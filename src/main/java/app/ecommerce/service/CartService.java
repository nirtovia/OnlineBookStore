package app.ecommerce.service;

import app.ecommerce.model.Book;
import app.ecommerce.model.Order;
import app.ecommerce.model.OrderItem;
import app.ecommerce.model.User;
import app.ecommerce.repository.BookRepository;
import app.ecommerce.repository.OrderRepository;
import app.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private HashMap<Long, Book> productHashMap = new HashMap<>();

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookService bookService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookRepository bookRepository;


    public void removeFromCart(Long productId) {
        if (productHashMap.containsKey(productId)) {
            productHashMap.remove(productId);
        } else {
            System.out.println("There is no");
        }
    }


    /**
     * This function is adding the products in the cart.
     *
     * @param productId
     */
    public void addToCart(Long productId, int quantity) {
        Optional<Book> product = bookRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setQuantityToBuy(quantity);
            productHashMap.put(product.get().getId(), product.get());
        } else {
            throw new RuntimeException("Book not found against this id:" + productId);
        }
    }


    /**
     * This function is getting used to combine the things to view the cart.
     *
     * @return
     */
    public List<Book> viewCart() {
        List<Book> books = new ArrayList<>();
        productHashMap.forEach(
                (key, value) -> books.add(value));
        return books;
    }

    /**
     * This is a checkout function where all the checkout bussiness logic is written.
     *
     * @return
     */
    @Transactional
    public Order checkout() {
        List<Book> books = viewCart();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isPresent()) {
            Order order = new Order();
            order.setOrderDateTime(LocalDateTime.now());

            // Create and associate OrderItem entities
            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0.0;

            for (Book book : books) {
                int quantity = book.getQuantityToBuy(); // Assuming you have a quantityToBuy field in Book
                if (quantity > 0) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(book);
                    orderItem.setQuantity(quantity);

                    totalPrice += book.getPrice() * quantity;

                    // Decrease the book's stock
                    int newStock = book.getStock() - quantity;
                    book.setStock(newStock);
                    bookRepository.save(book);

                    orderItems.add(orderItem);
                }
            }

            order.setOrderItems(orderItems);
            order.setTotalPrice(totalPrice);
            order.setUser(user.get());

            // Save the order and associated order items
            Order savedOrder = orderRepository.save(order);

            // Associate the order with the user and save the user
            user.get().getOrders().add(savedOrder);
            userRepository.save(user.get());

            // Clear the cart
            productHashMap.clear();

            return savedOrder;
        } else {
            throw new RuntimeException("User doesn't exist in the database with this username");
        }
    }
}
