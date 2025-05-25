package setup.EcommerceApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import setup.EcommerceApp.dto.CartItemDto;
import setup.EcommerceApp.dto.OrderResponseDto;
import setup.EcommerceApp.model.*;
import setup.EcommerceApp.repository.CartRepository;
import setup.EcommerceApp.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartItemRepository;
    @Autowired private OrderRepository orderRepository;

    public OrderResponseDto placeOrder(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getCost() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = cartItems.stream().map(cartItem -> {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            return oi;
        }).collect(Collectors.toList());

        order.setItems(items);
        orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setItems(items.stream().map(oi -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setQuantity(oi.getQuantity());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }

    public List<OrderResponseDto> getUserOrders(User user) {
        return orderRepository.findByUser(user).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setItems(order.getItems().stream().map(oi -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setQuantity(oi.getQuantity());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
