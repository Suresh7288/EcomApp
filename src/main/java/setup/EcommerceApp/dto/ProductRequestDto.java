package setup.EcommerceApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private Double cost;
    private Integer stock;
}
