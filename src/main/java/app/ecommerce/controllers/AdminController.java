package app.ecommerce.controllers;

import app.ecommerce.service.BookService;
import app.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    BookService bookService;
    @Autowired
    OrderService orderService;

    /**
     * This controller is rendering the view for the admin dashboard.
     * @param model
     * @return
     */
    @GetMapping("/dashboard")
    public String productsList(Model model) {
        model.addAttribute("productsList", bookService.getAllUnDeletedProducts());
        return "admin_dashboard";
    }

    /**
     * This controller is getting used to show all the orders that customers placed.
     * @param model
     * @return
     */
    @GetMapping("/allOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("ordersList", orderService.getAllOrders());
        return "orders";
    }
}
