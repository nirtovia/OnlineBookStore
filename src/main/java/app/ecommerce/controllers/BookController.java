package app.ecommerce.controllers;

import app.ecommerce.model.Book;
import app.ecommerce.service.BookService;
import app.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    CartService cartService;

    /**
     * This api will be getting used view the page to add new products
     *
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String getProductUploadForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "upload_product";
    }

    /**
     * This api will be getting used to create/add new products in the database.
     *
     * @param book
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public String saveUser(Book book) throws IOException {
        bookService.saveProduct(book);
        return "redirect:/admin/dashboard";
    }

    /**
     * This api will be getting used to view all the products that are in the database.
     *
     * @param model
     * @return
     */
    @GetMapping("/all")
    public String productsList(Model model) {
        int quantityValue = 0;
        model.addAttribute("quantityValue", quantityValue);
        model.addAttribute("productsList", bookService.getAllUnDeletedProducts());
        model.addAttribute("cartItems", cartService.viewCart());
        return "customer-dashboard";
    }

    /**
     * This controller will show us the details of a single product.
     *
     * @param model
     * @param productId
     * @return
     */
    @GetMapping("/getById/{id}")
    public String getProductById(Model model, @PathVariable(name = "id") Long productId) {
        model.addAttribute("book", bookService.getProductById(productId));
        return "single_product";
    }

    /**
     * This is getting used to delete a product from the frontend.
     *
     * @param productId
     * @return
     */
    @GetMapping("/deleteById/{id}")
    public String deleteProductById(@PathVariable(name = "id") Long productId) {
        bookService.deleteProduct(productId);
        return "redirect:/admin/dashboard";
    }

    /**
     * This is getting used to update a product in the database.
     *
     * @param model
     * @param productId
     * @return
     */
    @GetMapping("/update/{id}")
    public String updateProduct(Model model, @PathVariable(name = "id") Long productId) {
        model.addAttribute("productId", productId);
        model.addAttribute("book", bookService.getProductById(productId));
        return "update_product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable(name = "id") Long productId, Book book) throws IOException {
        book.setId(productId);
        bookService.saveProduct(book);
        return "redirect:/admin/dashboard";
    }
}
