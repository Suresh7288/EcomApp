package setup.EcommerceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import setup.EcommerceApp.model.Order;
import setup.EcommerceApp.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
