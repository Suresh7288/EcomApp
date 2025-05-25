package setup.EcommerceApp.dto;

import lombok.Getter;
import lombok.Setter;
import setup.EcommerceApp.model.Product;
import setup.EcommerceApp.model.User;

@Getter
@Setter
public class CartItemDto {
    private User user;
    private Integer quantity;
    private Product product;
}
