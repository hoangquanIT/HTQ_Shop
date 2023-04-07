package com.quanht.service;

import com.quanht.dto.CartDto;
import com.quanht.dto.CartItemDto;
import com.quanht.entities.*;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.AccountRepository;
import com.quanht.repositories.CartItemRepository;
import com.quanht.repositories.CartRepository;
import com.quanht.repositories.VariantRepository;
import com.quanht.request.CartRequest;
import com.quanht.security.ClientJwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ClientJwtUtils clientJwtUtils;
    private AccountRepository accountRepository;
    private VariantRepository variantRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ClientJwtUtils clientJwtUtils, AccountRepository accountRepository, VariantRepository variantRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.clientJwtUtils = clientJwtUtils;
        this.accountRepository = accountRepository;
        this.variantRepository = variantRepository;
    }

    public Cart getById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Giỏ hàng không tồn tại");
        });
    }

    public CartDto getCartDto(Long id){
        if (id == 0){
            return null;
        } else {
            Cart cart = getById(id);
            List<CartItem> items = cart.getCartItems();
            List<CartItemDto> itemDtos = new ArrayList<>();

            items.forEach(item -> {
                CartItemDto temp = getCartItemDto(item);
                if (temp != null){
                    itemDtos.add(temp);
                }
            });

            return CartDto.builder()
                    .note(cart.getNote())
                    .items(itemDtos)
                    .build();
        }
    }

    public CartItemDto getCartItemDto(CartItem cartItem) {
        Optional<Variant> variantOp = variantRepository.findById(cartItem.getVariantId());
        if (variantOp.isEmpty()) {
            cartItemRepository.delete(cartItem);
            return null;
        } else {
            Variant variant = variantOp.get();
            Product product = variant.getProduct();
            String url = product.getImages().get(0).getUrl();

            Double total = cartItem.getQuantity() * variant.getPrice();

            CartItemDto item = CartItemDto.builder()
                    .itemId(cartItem.getId())
                    .imageUrl(url)
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(cartItem.getQuantity())
                    .variant(variant)
                    .total(total)
                    .build();
            return item;
        }
    }

    @Transactional
    public CartRequest addToCart(CartRequest request, HttpServletRequest httpRequest) {

        CartItem cartItem = CartItem.builder()
                .variantId(request.getVariantId())
                .quantity(request.getQuantity())
                .build();

        Variant variant = variantRepository.findById(request.getVariantId()).get();
        variant.setQuantity(variant.getQuantity() - request.getQuantity());
        variantRepository.save(variant);

        if (request.getCartId() == 0) {
            Cart cart = new Cart();
            cartRepository.save(cart);

            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);

            request.setCartId(cart.getId());

            // Lấy token từ trong header của request
            String token = clientJwtUtils.getTokenFromCookie(httpRequest);
            if (token != null) {
                // Parse thông tin từ token
                Claims claims = clientJwtUtils.getClaimsFromToken(token);

                // Lấy username (email khách hàng)
                String userName = claims.getSubject();

                // Lấy thông tin khách hàng qua email
                Account account = accountRepository.findByEmail(userName).get();
                account.setCurrentCartId(cart.getId());
            }
        } else {
            Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(() -> {
                throw new NotFoundException("Giỏ hàng không tồn tại");
            });

            List<CartItem> items = cart.getCartItems();
            Optional<CartItem> myItem = items.stream()
                    .filter(item -> item.getVariantId().equals(cartItem.getVariantId()))
                    .findFirst();
            if (myItem.isPresent()) {
                CartItem oldItem = myItem.get();
                int newQty = oldItem.getQuantity() + cartItem.getQuantity();
                oldItem.setQuantity(newQty);
                cartItemRepository.save(oldItem);

            } else {
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
            }
        }
        return request;
    }

    @Transactional
    public void deleteCart(Long id){
        try {
            Cart cart = cartRepository.findById(id).get();
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.forEach(item -> {
                Variant variant = variantRepository.findById(item.getVariantId()).get();
                variant.setQuantity(variant.getQuantity() + item.getQuantity());
                variantRepository.save(variant);
                cartItemRepository.deleteById(item.getId());
            });
            cartRepository.delete(cart);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new BadRequestException("Xóa thất bại");
        }
    }

    @Transactional
    public void removeCartItem(Long id){
        try {
            CartItem cartItem = cartItemRepository.findById(id).get();
            Variant variant = variantRepository.findById(cartItem.getVariantId()).get();
            variant.setQuantity(variant.getQuantity() + cartItem.getQuantity());
            variantRepository.save(variant);
            cartItemRepository.deleteById(id);
        } catch (Exception e){
            throw new BadRequestException("Xóa thất bại");
        }
    }

}
