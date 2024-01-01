package app.ecommerce.repository;

import app.ecommerce.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * This is a custom query to get the products by their id.
     *
     * @param productId
     * @return
     */
    @Query(
            value = "SELECT * FROM books u WHERE u.id = ?1",
            nativeQuery = true)
    Book getById(Long productId);

    /**
     * This is a custom query to get all the products from the database..
     *
     * @return
     */
    @Query(
            value = "SELECT * FROM books",
            nativeQuery = true)
    List<Book> getAllProducts();


    /**
     * This is a custom query to get all the products that are not deleted.
     *
     * @return
     */
    @Query(
            value = "SELECT * FROM books where delete_status=0",
            nativeQuery = true)
    List<Book> getAllUnDeletedProducts();
}
