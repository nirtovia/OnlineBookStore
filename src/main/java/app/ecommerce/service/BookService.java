package app.ecommerce.service;

import app.ecommerce.model.Book;
import app.ecommerce.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    /**
     * This function is getting used to store the book in the database
     *
     * @param book
     * @throws IOException
     */
    public void saveProduct(Book book) {
        bookRepository.save(book);
    }

    /**
     * This function is getting used to fetch all the products from the database
     *
     * @return
     */
    public List<Book> getAllProducts() {
        return bookRepository.getAllProducts();
    }

    /**
     * This function is getting used to fetch all the products that are not deleted.
     * Like which status is 0 means false.
     * That shows these products can be viewed to the frontend.
     *
     * @return
     */
    public List<Book> getAllUnDeletedProducts() {
        return bookRepository.getAllUnDeletedProducts();
    }

    /**
     * This function is getting used to get the product by its id.
     *
     * @param productId
     * @return
     */
    public Book getProductById(Long productId) {
        return bookRepository.getById(productId);
    }

    /**
     * This function is getting used to delete the product by its id.
     *
     * @param productId
     * @return
     */
    public void deleteProduct(Long productId) {
        Optional<Book> product = bookRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setDeleteStatus(true);
            bookRepository.save(product.get());
        }
    }
}
