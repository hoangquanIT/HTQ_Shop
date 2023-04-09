package com.quanht.service;

import com.quanht.dto.CartDto;
import com.quanht.dto.WebOrderDto;
import com.quanht.entities.*;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.*;
import com.quanht.request.OrderCreateRequest;
import com.quanht.security.ClientJwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private VariantRepository variantRepository;
    private ClientJwtUtils clientJwtUtils;
    private AccountRepository accountRepository;
    private ShippingAddressRepository shippingAddressRepository;
    private CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        VariantRepository variantRepository, ClientJwtUtils clientJwtUtils,
                        AccountRepository accountRepository, ShippingAddressRepository shippingAddressRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.variantRepository = variantRepository;
        this.clientJwtUtils = clientJwtUtils;
        this.accountRepository = accountRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order createClientOrder(HttpServletRequest request, OrderCreateRequest orderRequest) {
        Optional<Order> oldOrder = orderRepository.findByCartId(orderRequest.getCartId());
        if (oldOrder.isPresent()) {
            Order newOrder = oldOrder.get();
            newOrder.setNote(orderRequest.getNote());
            newOrder.setTotal(orderRequest.getTotal());
            orderRepository.save(newOrder);

            createOderItem(newOrder, orderRequest, false);

            Optional<ShippingAddress> oldShippingAddress = shippingAddressRepository
                    .findById(newOrder.getShippingAddress().getId());
            if (oldShippingAddress.isPresent()) {
                ShippingAddress newAddress = oldShippingAddress.get();
                newAddress.setName(orderRequest.getShippingAddress().getName());
                newAddress.setPhone(orderRequest.getShippingAddress().getPhone());
                newAddress.setEmail(orderRequest.getShippingAddress().getEmail());
                newAddress.setAddress(orderRequest.getShippingAddress().getAddress());
                newAddress.setCity(orderRequest.getShippingAddress().getCity());
                shippingAddressRepository.save(newAddress);
            }

            return newOrder;
        } else {
            Order order = Order.builder()
                    .note(orderRequest.getNote())
                    .total(orderRequest.getTotal())
                    .cartId(orderRequest.getCartId())
                    .account(getCustomer(request))
                    .build();
            orderRepository.save(order);

            createOderItem(order, orderRequest, true);

            ShippingAddress shippingAddress = orderRequest.getShippingAddress();
            shippingAddress.setOrder(order);
            shippingAddressRepository.save(shippingAddress);

            return order;
        }
    }

    public Account getCustomer(HttpServletRequest request){
        // Lấy token từ trong header của request
        String token = clientJwtUtils.getTokenFromCookie(request);

        if(token != null){
            // Parse thông tin từ token
            Claims claims = clientJwtUtils.getClaimsFromToken(token);

            // Lấy username (email khách hàng)
            String userName = claims.getSubject();

            // Lấy thông tin khách hàng qua email
            Account customer = accountRepository.findByEmail(userName).orElseThrow(() -> {
                throw new NotFoundException("User không tồn tại");
            });

            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public void createOderItem(Order order, OrderCreateRequest orderRequest, Boolean isNew){
        if (!isNew) {
            List<OrderItem> orderItems = orderRequest.getOrderItems();
            List<OrderItem> oldItems = order.getOrderItems();
            int numberOfNewItems = orderItems.size();
            int numberOfOldItems = oldItems.size();

            OrderItem newItem = null;
            OrderItem oldItem = null;
            boolean check = true;
            for (int i = 0; i < numberOfNewItems; i++) {
                newItem = orderItems.get(i);
                check = true;
                for (int j = 0; j < numberOfOldItems; j++) {
                    oldItem = orderItemRepository.findById(oldItems.get(j).getId()).orElse(null);
                    if (oldItem != null && oldItem.getVariantId() == newItem.getVariantId()) {
                        oldItem.setQuantity(newItem.getQuantity());
                        orderItemRepository.save(oldItem);
                        check = false;
                        break;
                    }
                }
                if (check) {
                    newItem.setOrder(order);
                    orderItemRepository.save(newItem);
                }
            }

            for (int i = 0; i < numberOfOldItems; i++) {
                oldItem = oldItems.get(i);
                check = false;
                for (int j = 0; j < numberOfNewItems; j++) {
                    newItem = orderItems.get(j);
                    if (oldItem.getVariantId() == newItem.getVariantId()) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    orderItemRepository.customDeleteById(oldItem.getId());
                }
            }
        } else {
            List<OrderItem> orderItems = orderRequest.getOrderItems();
            orderItems.forEach(item -> {
                item.setOrder(order);
                orderItemRepository.save(item);
            });
        }
    }

    public WebOrderDto getClientOrderInfo(Long cartId){
        CartDto cartDto = cartService.getCartDto(cartId);
        Order order = orderRepository.findByCartId(cartId).get();

        return WebOrderDto.builder()
                .orderCode(order.getId())
                .items(cartDto.getItems())
                .shippingAddress(order.getShippingAddress())
                .build();
    }

    public WebOrderDto getOrderInfoAfterPayment(String orderId) {
        Order order = orderRepository.findById(orderId).get();
        CartDto cartDto = cartService.getCartDto(order.getCartId());

        return WebOrderDto.builder()
                .orderCode(orderId)
                .items(cartDto.getItems())
                .shippingAddress(order.getShippingAddress())
                .build();
    }

    @Transactional
    public Order updateOrderPayment(String orderId) {
        Order order = orderRepository.findById(orderId).get();

        order.setPayment(OrderPayment.PAID);
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        return order;
    }

}
