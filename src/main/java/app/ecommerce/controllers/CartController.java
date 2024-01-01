package app.ecommerce.controllers;

import app.ecommerce.model.Book;
import app.ecommerce.model.Order;
import app.ecommerce.repository.BookRepository;
import app.ecommerce.service.BookService;
import app.ecommerce.service.CartService;
import app.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    /**
     * This controller is getting used to add the products in the cart.
     * It is accepting product id which we have to add in the cart.
     *
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable(name = "id") Long productId, @RequestParam(name = "quantity") int quantity, Model model) {
        if (quantity <= 0) {
            return "redirect:/";
        }

        Optional<Book> product = bookRepository.findById(productId);
        if (product.isPresent() && quantity > product.get().getStock()) {
            model.addAttribute("OutOfStock", true);
            return "customer-dashboard";
        }
        cartService.addToCart(productId, quantity);

        model.addAttribute("productsList", bookService.getAllProducts());
        model.addAttribute("cartItems", cartService.viewCart());
        return "redirect:/cart/view";
    }


    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable(name = "id") Long productId, Model model) {
        cartService.removeFromCart(productId);
        return "redirect:/cart/view";
    }

    /**
     * This controller is getting used to view the cart page.
     * It will show the cart page and all the products that are added in the cart yet.
     *
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String viewCart(Model model) {
        List<Book> productsInCart = cartService.viewCart();
        Double totalPrice = 0.0;
        for (Book book : productsInCart
        ) {
            totalPrice = totalPrice + book.getPrice() * book.getQuantityToBuy();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartService.viewCart());
        return "shopping_cart";
    }


    /**
     * This is getting used for the final checkout where after finishing shopping,
     * When the user will click on checkout button, this api will get the hit.
     *
     * @param model
     * @return
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cartService.viewCart().isEmpty()) {
            model.addAttribute("emptyCart", true);
            return "shopping_cart";
        }
        Order order = cartService.checkout();
        model.addAttribute("order", order);
        return "checkout";
    }

}
