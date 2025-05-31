package setup.EcommerceApp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import setup.EcommerceApp.dto.CartItemDto;
import setup.EcommerceApp.model.CartItem;
import setup.EcommerceApp.model.Product;
import setup.EcommerceApp.model.User;
import setup.EcommerceApp.repository.CartRepository;
import setup.EcommerceApp.repository.ProductRepository;
import setup.EcommerceApp.repository.UserRepository;

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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository){
        this.cartRepository=cartRepository;
        this.productRepository = productRepository;
        this.userRepository=userRepository;
    }
    public void addCart(UserDetails userdetails, Long productId, Integer quantity){
        Product product = productRepository.findById(productId).orElseThrow(()-> new NoSuchElementException("Product not found"));
        User user = userRepository.findByEmail(userdetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
