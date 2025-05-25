package setup.EcommerceApp.dto;

import lombok.Getter;
import lombok.Setter;
import setup.EcommerceApp.model.OrderStatus;
import setup.EcommerceApp.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private List<CartItemDto> items;
}
