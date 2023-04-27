package com.quanht.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quanht.entities.Account;
import com.quanht.entities.Order;
import com.quanht.entities.OrderFulfillment;
import com.quanht.entities.OrderItem;
import com.quanht.entities.OrderPayment;
import com.quanht.entities.OrderStatus;
import com.quanht.entities.ShippingAddress;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.AccountRepository;
import com.quanht.repositories.OrderItemRepository;
import com.quanht.repositories.OrderRepository;
import com.quanht.repositories.ShippingAddressRepository;
import com.quanht.repositories.VariantRepository;
import com.quanht.request.OrderCreateRequest;
import com.quanht.security.ClientJwtUtils;
import io.jsonwebtoken.impl.DefaultClaims;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
class OrderServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CartService cartService;

    @MockBean
    private ClientJwtUtils clientJwtUtils;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockBean
    private ShippingAddressRepository shippingAddressRepository;

    @MockBean
    private VariantRepository variantRepository;

    /**
     * Method under test: {@link OrderService#createClientOrder(HttpServletRequest, OrderCreateRequest)}
     */
    @Test
    void testCreateClientOrder2() {
        Account account = new Account();
        account.setAddress("42 Main St");
        account.setCityCode(1);
        account.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account.setCurrentCartId(1L);
        account.setEmail("jane.doe@example.org");
        account.setEnabled(true);
        account.setId(1L);
        account.setName("Name");
        account.setOrders(new ArrayList<>());
        account.setPassword("iloveyou");
        account.setPhone("6625550144");
        account.setRoles(new ArrayList<>());
        account.setTokens(new ArrayList<>());
        account.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Account account2 = new Account();
        account2.setAddress("42 Main St");
        account2.setCityCode(1);
        account2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account2.setCurrentCartId(1L);
        account2.setEmail("jane.doe@example.org");
        account2.setEnabled(true);
        account2.setId(1L);
        account2.setName("Name");
        account2.setOrders(new ArrayList<>());
        account2.setPassword("iloveyou");
        account2.setPhone("6625550144");
        account2.setRoles(new ArrayList<>());
        account2.setTokens(new ArrayList<>());
        account2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Order order = new Order();
        order.setAccount(new Account());
        order.setCartId(1L);
        order.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setFulfillment(OrderFulfillment.UNFULFILLED);
        order.setId("42");
        order.setNote("Note");
        order.setOrderItems(new ArrayList<>());
        order.setPayment(OrderPayment.UNPAID);
        order.setShippingAddress(new ShippingAddress());
        order.setStatus(OrderStatus.NEW);
        order.setTotal(10.0d);
        order.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("42 Main St");
        shippingAddress.setCity("Oxford");
        shippingAddress.setEmail("jane.doe@example.org");
        shippingAddress.setId(1L);
        shippingAddress.setName("Name");
        shippingAddress.setOrder(order);
        shippingAddress.setPhone("6625550144");

        Order order2 = new Order();
        order2.setAccount(account2);
        order2.setCartId(1L);
        order2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order2.setFulfillment(OrderFulfillment.UNFULFILLED);
        order2.setId("42");
        order2.setNote("Note");
        order2.setOrderItems(new ArrayList<>());
        order2.setPayment(OrderPayment.UNPAID);
        order2.setShippingAddress(shippingAddress);
        order2.setStatus(OrderStatus.NEW);
        order2.setTotal(10.0d);
        order2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress2 = new ShippingAddress();
        shippingAddress2.setAddress("42 Main St");
        shippingAddress2.setCity("Oxford");
        shippingAddress2.setEmail("jane.doe@example.org");
        shippingAddress2.setId(1L);
        shippingAddress2.setName("Name");
        shippingAddress2.setOrder(order2);
        shippingAddress2.setPhone("6625550144");

        Order order3 = new Order();
        order3.setAccount(account);
        order3.setCartId(1L);
        order3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order3.setFulfillment(OrderFulfillment.UNFULFILLED);
        order3.setId("42");
        order3.setNote("Note");
        order3.setOrderItems(new ArrayList<>());
        order3.setPayment(OrderPayment.UNPAID);
        order3.setShippingAddress(shippingAddress2);
        order3.setStatus(OrderStatus.NEW);
        order3.setTotal(10.0d);
        order3.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order3);
        when(clientJwtUtils.getClaimsFromToken(Mockito.<String>any())).thenReturn(new DefaultClaims());
        when(clientJwtUtils.getTokenFromCookie(Mockito.<HttpServletRequest>any())).thenReturn("ABC123");
        when(accountRepository.findByEmail(Mockito.<String>any())).thenThrow(new NotFoundException("An error occurred"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThrows(NotFoundException.class, () -> orderService.createClientOrder(request, new OrderCreateRequest()));
        verify(clientJwtUtils).getClaimsFromToken(Mockito.<String>any());
        verify(clientJwtUtils).getTokenFromCookie(Mockito.<HttpServletRequest>any());
        verify(accountRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link OrderService#createClientOrder(HttpServletRequest, OrderCreateRequest)}
     */
    @Test
    void testCreateClientOrder4() {
        Account account = new Account();
        account.setAddress("42 Main St");
        account.setCityCode(1);
        account.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account.setCurrentCartId(1L);
        account.setEmail("jane.doe@example.org");
        account.setEnabled(true);
        account.setId(1L);
        account.setName("Name");
        account.setOrders(new ArrayList<>());
        account.setPassword("iloveyou");
        account.setPhone("6625550144");
        account.setRoles(new ArrayList<>());
        account.setTokens(new ArrayList<>());
        account.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Account account2 = new Account();
        account2.setAddress("42 Main St");
        account2.setCityCode(1);
        account2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account2.setCurrentCartId(1L);
        account2.setEmail("jane.doe@example.org");
        account2.setEnabled(true);
        account2.setId(1L);
        account2.setName("Name");
        account2.setOrders(new ArrayList<>());
        account2.setPassword("iloveyou");
        account2.setPhone("6625550144");
        account2.setRoles(new ArrayList<>());
        account2.setTokens(new ArrayList<>());
        account2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Order order = new Order();
        order.setAccount(new Account());
        order.setCartId(1L);
        order.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setFulfillment(OrderFulfillment.UNFULFILLED);
        order.setId("42");
        order.setNote("Note");
        order.setOrderItems(new ArrayList<>());
        order.setPayment(OrderPayment.UNPAID);
        order.setShippingAddress(new ShippingAddress());
        order.setStatus(OrderStatus.NEW);
        order.setTotal(10.0d);
        order.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("42 Main St");
        shippingAddress.setCity("Oxford");
        shippingAddress.setEmail("jane.doe@example.org");
        shippingAddress.setId(1L);
        shippingAddress.setName("Name");
        shippingAddress.setOrder(order);
        shippingAddress.setPhone("6625550144");

        Order order2 = new Order();
        order2.setAccount(account2);
        order2.setCartId(1L);
        order2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order2.setFulfillment(OrderFulfillment.UNFULFILLED);
        order2.setId("42");
        order2.setNote("Note");
        order2.setOrderItems(new ArrayList<>());
        order2.setPayment(OrderPayment.UNPAID);
        order2.setShippingAddress(shippingAddress);
        order2.setStatus(OrderStatus.NEW);
        order2.setTotal(10.0d);
        order2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress2 = new ShippingAddress();
        shippingAddress2.setAddress("42 Main St");
        shippingAddress2.setCity("Oxford");
        shippingAddress2.setEmail("jane.doe@example.org");
        shippingAddress2.setId(1L);
        shippingAddress2.setName("Name");
        shippingAddress2.setOrder(order2);
        shippingAddress2.setPhone("6625550144");

        Order order3 = new Order();
        order3.setAccount(account);
        order3.setCartId(1L);
        order3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order3.setFulfillment(OrderFulfillment.UNFULFILLED);
        order3.setId("42");
        order3.setNote("Note");
        order3.setOrderItems(new ArrayList<>());
        order3.setPayment(OrderPayment.UNPAID);
        order3.setShippingAddress(shippingAddress2);
        order3.setStatus(OrderStatus.NEW);
        order3.setTotal(10.0d);
        order3.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order3);
        when(clientJwtUtils.getClaimsFromToken(Mockito.<String>any())).thenReturn(new DefaultClaims());
        when(clientJwtUtils.getTokenFromCookie(Mockito.<HttpServletRequest>any())).thenReturn("ABC123");
        when(accountRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThrows(NotFoundException.class, () -> orderService.createClientOrder(request, new OrderCreateRequest()));
        verify(clientJwtUtils).getClaimsFromToken(Mockito.<String>any());
        verify(clientJwtUtils).getTokenFromCookie(Mockito.<HttpServletRequest>any());
        verify(accountRepository).findByEmail(Mockito.<String>any());
    }

    /** thanh cong
     * Method under test: {@link OrderService#createClientOrder(HttpServletRequest, OrderCreateRequest)}
     */
    @Test
    void testCreateClientOrder6() {
        Account account = new Account();
        account.setAddress("42 Main St");
        account.setCityCode(1);
        account.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account.setCurrentCartId(1L);
        account.setEmail("jane.doe@example.org");
        account.setEnabled(true);
        account.setId(1L);
        account.setName("Name");
        account.setOrders(new ArrayList<>());
        account.setPassword("iloveyou");
        account.setPhone("6625550144");
        account.setRoles(new ArrayList<>());
        account.setTokens(new ArrayList<>());
        account.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Account account2 = new Account();
        account2.setAddress("42 Main St");
        account2.setCityCode(1);
        account2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account2.setCurrentCartId(1L);
        account2.setEmail("jane.doe@example.org");
        account2.setEnabled(true);
        account2.setId(1L);
        account2.setName("Name");
        account2.setOrders(new ArrayList<>());
        account2.setPassword("iloveyou");
        account2.setPhone("6625550144");
        account2.setRoles(new ArrayList<>());
        account2.setTokens(new ArrayList<>());
        account2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Order order = new Order();
        order.setAccount(new Account());
        order.setCartId(1L);
        order.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setFulfillment(OrderFulfillment.UNFULFILLED);
        order.setId("42");
        order.setNote("Note");
        order.setOrderItems(new ArrayList<>());
        order.setPayment(OrderPayment.UNPAID);
        order.setShippingAddress(new ShippingAddress());
        order.setStatus(OrderStatus.NEW);
        order.setTotal(10.0d);
        order.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("42 Main St");
        shippingAddress.setCity("Oxford");
        shippingAddress.setEmail("jane.doe@example.org");
        shippingAddress.setId(1L);
        shippingAddress.setName("Name");
        shippingAddress.setOrder(order);
        shippingAddress.setPhone("6625550144");

        Order order2 = new Order();
        order2.setAccount(account2);
        order2.setCartId(1L);
        order2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order2.setFulfillment(OrderFulfillment.UNFULFILLED);
        order2.setId("42");
        order2.setNote("Note");
        order2.setOrderItems(new ArrayList<>());
        order2.setPayment(OrderPayment.UNPAID);
        order2.setShippingAddress(shippingAddress);
        order2.setStatus(OrderStatus.NEW);
        order2.setTotal(10.0d);
        order2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress2 = new ShippingAddress();
        shippingAddress2.setAddress("42 Main St");
        shippingAddress2.setCity("Oxford");
        shippingAddress2.setEmail("jane.doe@example.org");
        shippingAddress2.setId(1L);
        shippingAddress2.setName("Name");
        shippingAddress2.setOrder(order2);
        shippingAddress2.setPhone("6625550144");

        Order order3 = new Order();
        order3.setAccount(account);
        order3.setCartId(1L);
        order3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order3.setFulfillment(OrderFulfillment.UNFULFILLED);
        order3.setId("42");
        order3.setNote("Note");
        order3.setOrderItems(new ArrayList<>());
        order3.setPayment(OrderPayment.UNPAID);
        order3.setShippingAddress(shippingAddress2);
        order3.setStatus(OrderStatus.NEW);
        order3.setTotal(10.0d);
        order3.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order3);
        when(clientJwtUtils.getClaimsFromToken(Mockito.<String>any())).thenReturn(new DefaultClaims());
        when(clientJwtUtils.getTokenFromCookie(Mockito.<HttpServletRequest>any())).thenReturn("ABC123");

        Account account3 = new Account();
        account3.setAddress("42 Main St");
        account3.setCityCode(1);
        account3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account3.setCurrentCartId(1L);
        account3.setEmail("jane.doe@example.org");
        account3.setEnabled(true);
        account3.setId(1L);
        account3.setName("Name");
        account3.setOrders(new ArrayList<>());
        account3.setPassword("iloveyou");
        account3.setPhone("6625550144");
        account3.setRoles(new ArrayList<>());
        account3.setTokens(new ArrayList<>());
        account3.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<Account> ofResult = Optional.of(account3);
        when(accountRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        Account account4 = new Account();
        account4.setAddress("42 Main St");
        account4.setCityCode(1);
        account4.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account4.setCurrentCartId(1L);
        account4.setEmail("jane.doe@example.org");
        account4.setEnabled(true);
        account4.setId(1L);
        account4.setName("Name");
        account4.setOrders(new ArrayList<>());
        account4.setPassword("iloveyou");
        account4.setPhone("6625550144");
        account4.setRoles(new ArrayList<>());
        account4.setTokens(new ArrayList<>());
        account4.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        Account account5 = new Account();
        account5.setAddress("42 Main St");
        account5.setCityCode(1);
        account5.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account5.setCurrentCartId(1L);
        account5.setEmail("jane.doe@example.org");
        account5.setEnabled(true);
        account5.setId(1L);
        account5.setName("Name");
        account5.setOrders(new ArrayList<>());
        account5.setPassword("iloveyou");
        account5.setPhone("6625550144");
        account5.setRoles(new ArrayList<>());
        account5.setTokens(new ArrayList<>());
        account5.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress3 = new ShippingAddress();
        shippingAddress3.setAddress("42 Main St");
        shippingAddress3.setCity("Oxford");
        shippingAddress3.setEmail("jane.doe@example.org");
        shippingAddress3.setId(1L);
        shippingAddress3.setName("Name");
        shippingAddress3.setOrder(new Order());
        shippingAddress3.setPhone("6625550144");

        Order order4 = new Order();
        order4.setAccount(account5);
        order4.setCartId(1L);
        order4.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order4.setFulfillment(OrderFulfillment.UNFULFILLED);
        order4.setId("42");
        order4.setNote("Note");
        order4.setOrderItems(new ArrayList<>());
        order4.setPayment(OrderPayment.UNPAID);
        order4.setShippingAddress(shippingAddress3);
        order4.setStatus(OrderStatus.NEW);
        order4.setTotal(10.0d);
        order4.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress4 = new ShippingAddress();
        shippingAddress4.setAddress("42 Main St");
        shippingAddress4.setCity("Oxford");
        shippingAddress4.setEmail("jane.doe@example.org");
        shippingAddress4.setId(1L);
        shippingAddress4.setName("Name");
        shippingAddress4.setOrder(order4);
        shippingAddress4.setPhone("6625550144");

        Order order5 = new Order();
        order5.setAccount(account4);
        order5.setCartId(1L);
        order5.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        order5.setFulfillment(OrderFulfillment.UNFULFILLED);
        order5.setId("42");
        order5.setNote("Note");
        order5.setOrderItems(new ArrayList<>());
        order5.setPayment(OrderPayment.UNPAID);
        order5.setShippingAddress(shippingAddress4);
        order5.setStatus(OrderStatus.NEW);
        order5.setTotal(10.0d);
        order5.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ShippingAddress shippingAddress5 = new ShippingAddress();
        shippingAddress5.setAddress("42 Main St");
        shippingAddress5.setCity("Oxford");
        shippingAddress5.setEmail("jane.doe@example.org");
        shippingAddress5.setId(1L);
        shippingAddress5.setName("Name");
        shippingAddress5.setOrder(order5);
        shippingAddress5.setPhone("6625550144");
        when(shippingAddressRepository.save(Mockito.<ShippingAddress>any())).thenReturn(shippingAddress5);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        ShippingAddress shippingAddress6 = new ShippingAddress();
        Order actualCreateClientOrderResult = orderService.createClientOrder(request,
                new OrderCreateRequest("Note", 10.0d, 1L, orderItems, shippingAddress6, new Account()));
        assertSame(account3, actualCreateClientOrderResult.getAccount());
        assertEquals(1L, actualCreateClientOrderResult.getCartId().longValue());
        assertNull(actualCreateClientOrderResult.getFulfillment());
        assertNull(actualCreateClientOrderResult.getPayment());
        assertNull(actualCreateClientOrderResult.getShippingAddress());
        assertNull(actualCreateClientOrderResult.getId());
        assertNull(actualCreateClientOrderResult.getStatus());
        assertEquals(10.0d, actualCreateClientOrderResult.getTotal().doubleValue());
        assertEquals("Note", actualCreateClientOrderResult.getNote());
        assertNull(actualCreateClientOrderResult.getOrderItems());
        verify(orderRepository).save(Mockito.<Order>any());
        verify(clientJwtUtils).getClaimsFromToken(Mockito.<String>any());
        verify(clientJwtUtils).getTokenFromCookie(Mockito.<HttpServletRequest>any());
        verify(accountRepository).findByEmail(Mockito.<String>any());
        verify(shippingAddressRepository).save(Mockito.<ShippingAddress>any());
    }
}

