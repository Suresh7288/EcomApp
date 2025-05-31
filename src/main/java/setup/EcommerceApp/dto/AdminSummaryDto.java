package setup.EcommerceApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSummaryDto {
    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
}
