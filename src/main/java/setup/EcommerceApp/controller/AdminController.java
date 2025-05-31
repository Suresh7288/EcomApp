package setup.EcommerceApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import setup.EcommerceApp.dto.AdminSummaryDto;
import setup.EcommerceApp.dto.OrderStatsDto;
import setup.EcommerceApp.dto.ProductResponseDto;
import setup.EcommerceApp.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryDto> getSummary() {
        return ResponseEntity.ok(adminService.getAdminSummary());
    }

    @GetMapping("/orders/stats")
    public ResponseEntity<List<OrderStatsDto>> getOrderStats() {
        return ResponseEntity.ok(adminService.getOrderStats());
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {
        return ResponseEntity.ok(adminService.getLowStockProducts(threshold));
    }
}
