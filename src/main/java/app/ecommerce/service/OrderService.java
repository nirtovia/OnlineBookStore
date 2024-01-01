package app.ecommerce.service;

import app.ecommerce.model.Book;
import app.ecommerce.model.Order;
import app.ecommerce.model.OrderItem;
import app.ecommerce.model.User;
import app.ecommerce.repository.OrderRepository;
import app.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * Function to get all the orders from the database.
     *
     * @return
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Function to get the specific user orders.
     *
     * @param username
     * @return
     */
    public List<Order> getOrdersByUser(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getOrders();
        } else {
            throw new RuntimeException("User doesn't exists in the database against this username");
        }
    }

    /**
     * Function to fetch the products from an order.
     *
     * @param orderId
     * @return
     */
    public List<OrderItem> getBooksByOrderId(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get().getOrderItems();
        } else {
            throw new RuntimeException("There is no order against this order Id: " + orderId);
        }
    }

    /**
     * Function to fetch the customer from an order.
     *
     * @param orderId
     * @return
     */
    public User getCustomerFromAnOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get().getUser();
        } else {
            throw new RuntimeException("There is no order against this order Id: " + orderId);
        }
    }
}
