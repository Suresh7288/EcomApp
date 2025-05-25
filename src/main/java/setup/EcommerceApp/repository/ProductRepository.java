package setup.EcommerceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import setup.EcommerceApp.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    static Optional<Product> findById(Product productId) {
        return null;
    }

}
