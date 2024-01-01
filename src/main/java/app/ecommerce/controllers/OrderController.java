package app.ecommerce.controllers;

import app.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * This controller is getting used to view a single order.
     * Like If the user want to view the products in the order that he purchased.
     * This will be used for that.
     *
     * @param orderId
     * @param model
     * @return
     */
    @GetMapping("/getById/{id}")
    public String getProductsByOrderId(@PathVariable(name = "id") Long orderId, Model model) {
        model.addAttribute("orderedItems", orderService.getBooksByOrderId(orderId));
        return "order_products";
    }

    /**
     * This api is getting view the customer of an order.
     * It will show which customer placed that specific order.
     *
     * @param orderId
     * @param model
     * @return
     */
    @GetMapping("/getCustomerByOrderId/{id}")
    public String getCustomerByOrder(@PathVariable(name = "id") Long orderId, Model model) {
        model.addAttribute("customer", orderService.getCustomerFromAnOrder(orderId));
        return "order_customer";
    }
}
