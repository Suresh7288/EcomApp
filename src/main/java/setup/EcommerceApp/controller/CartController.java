package setup.EcommerceApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import setup.EcommerceApp.dto.CartItemDto;
import setup.EcommerceApp.model.Product;
import setup.EcommerceApp.model.User;
import setup.EcommerceApp.service.CartService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addCart(@AuthenticationPrincipal User user, @RequestParam Product productId, @RequestParam Integer quantity){
        cartService.addCart(user,productId, quantity);
        return ResponseEntity.ok("added to cart");
    }
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getAllCartItems(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(cartService.getAllCartItems(user));
    }
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId, @AuthenticationPrincipal User user) throws AccessDeniedException {
        cartService.removeCartItem(itemId, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.deleteCart(user);
        return ResponseEntity.noContent().build();
    }
}
