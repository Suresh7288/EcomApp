package setup.EcommerceApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ProductResponseDto {
    private String name;
    private String description;
    private String category;
    private Integer stock;
    private Double cost;
    private LocalDateTime createdAt;
    private String imageUrl;
}
