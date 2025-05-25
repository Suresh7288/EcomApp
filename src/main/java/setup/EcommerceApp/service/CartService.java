package setup.EcommerceApp.service;

import org.springframework.stereotype.Service;
import setup.EcommerceApp.dto.CartItemDto;
import setup.EcommerceApp.model.CartItem;
import setup.EcommerceApp.model.Product;
import setup.EcommerceApp.model.User;
import setup.EcommerceApp.repository.CartRepository;
import setup.EcommerceApp.repository.ProductRepository;

import javax.sound.sampled.Port;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartService {
    private  CartRepository cartRepository;
    private ProductRepository productRepository;
    public CartService(CartRepository cartRepository, ProductRepository productRepository){
        this.cartRepository=cartRepository;
        this.productRepository = productRepository;
    }
    public void addCart(User user, Product productId, Integer quantity){
        assert Objects.requireNonNull(ProductRepository.findById(productId)).isPresent();
        Product product = ProductRepository.findById(productId).orElseThrow(()-> new NoSuchElementException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setAddTime(LocalDateTime.now());
        cartRepository.save(cartItem);
    }
    public List<CartItemDto> getAllCartItems(User user){
        return cartRepository.findByUser(user).stream().map(item->{
            CartItemDto dto = new CartItemDto();
            dto.setProduct(item.getProduct());
            dto.setQuantity(item.getQuantity());
            dto.setUser(item.getUser());
            return dto;

        }).collect(Collectors.toList());
    }
    public void removeCartItem(Long itemId, User user) throws AccessDeniedException {
        CartItem item =cartRepository.findById(itemId).orElseThrow(()-> new NoSuchElementException("No Item Found"));
        if(!item.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized to delete item");
        }
            cartRepository.delete(item);
    }
    public void deleteCart(User user){
        cartRepository.deleteByUser(user);
    }
}
