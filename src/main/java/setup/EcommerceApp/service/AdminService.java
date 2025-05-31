package setup.EcommerceApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import setup.EcommerceApp.dto.AdminSummaryDto;
import setup.EcommerceApp.dto.OrderStatsDto;
import setup.EcommerceApp.dto.ProductResponseDto;
import setup.EcommerceApp.repository.OrderRepository;
import setup.EcommerceApp.repository.ProductRepository;
import setup.EcommerceApp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    public AdminSummaryDto getAdminSummary() {
        AdminSummaryDto dto = new AdminSummaryDto();
        dto.setTotalUsers(userRepository.count());
        dto.setTotalProducts(productRepository.count());
        dto.setTotalOrders(orderRepository.count());
        return dto;
    }

    public List<OrderStatsDto> getOrderStats() {
        List<Object[]> rawStats = orderRepository.countOrdersByStatus();
        return rawStats.stream().map(obj -> {
            OrderStatsDto dto = new OrderStatsDto();
            dto.setStatus(String.valueOf(obj[0]));
            dto.setCount((Long) obj[1]);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ProductResponseDto> getLowStockProducts(int threshold) {
        return productRepository.findByStockLessThan(threshold)
                .stream()
                .map(product -> {
                    ProductResponseDto dto = new ProductResponseDto();
                    dto.setName(product.getName());
                    dto.setStock(product.getStock());
                    dto.setImageUrl(product.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}


