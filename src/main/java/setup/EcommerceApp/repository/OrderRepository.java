package setup.EcommerceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import setup.EcommerceApp.model.Order;
import setup.EcommerceApp.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    @Query("SELECT o.orderStatus AS status, COUNT(o) AS count FROM Order o GROUP BY o.orderStatus")
    List<Object[]> countOrdersByStatus();
}
