package app.ecommerce.controllers;

import app.ecommerce.model.User;
import app.ecommerce.service.BookService;
import app.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    /**
     * This controller is getting used to differentiate the admin panel and customer panel.
     * Like if the logged in user is an admin, then he will go to admin dashboard.
     * else he will be going to customer dashboard.
     *
     * @return
     */
    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent() && (user.get().getRole().getName().equalsIgnoreCase("ROLE_MEMBER") ||
                user.get().getRole().getName().equalsIgnoreCase("ROLE_GUEST")
        )) {
            return "redirect:/book/all";
        } else if (user.isPresent() && user.get().getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("booksList", bookService.getAllUnDeletedProducts());
            return "index";
        }
    }
}
