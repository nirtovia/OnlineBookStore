package app.ecommerce;

import app.ecommerce.model.Book;
import app.ecommerce.model.Role;
import app.ecommerce.model.User;
import app.ecommerce.repository.BookRepository;
import app.ecommerce.repository.RoleRepository;
import app.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookOrderingSystem {

    public static void main(String[] args) {
        SpringApplication.run(BookOrderingSystem.class, args);
    }

    /**
     * Here I have created a admin account which will be created when there is no admin account.
     * So, every time I'll check whether this admin account exists in our database or not, if not then add this.
     * This admin account will be the admin of all the products that will be storing the products and all other things.
     *
     * @param userRepository
     * @param passwordEncoder
     * @param roleRepository
     * @return
     */
    @Bean
    CommandLineRunner run(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, BookRepository bookRepository) {
        return args -> {

            Role savedAdminRole = new Role();

            if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
                Role adminRole = new Role();
                adminRole.setId(1L);
                adminRole.setName("ROLE_ADMIN");
                savedAdminRole = roleRepository.save(adminRole);
            }

            if (!userRepository.findUserByUsername("admin").isPresent()) {
                User admin = new User();
                admin.setId(1L);
                admin.setFirstName("Admin");
                admin.setLastName("Account");
                admin.setAddress("Tel-Aviv");
                admin.setEmail("admin@gmail.com");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(savedAdminRole);
                userRepository.save(admin);
            }

            Role memberSavedRole = new Role();

            if (!roleRepository.findByName("ROLE_MEMBER").isPresent()) {
                Role memberRole = new Role();
                memberRole.setId(2L);
                memberRole.setName("ROLE_MEMBER");
                memberSavedRole = roleRepository.save(memberRole);
            }

            if (!userRepository.findUserByUsername("member").isPresent()) {
                User member = new User();
                member.setId(2L);
                member.setFirstName("Member");
                member.setLastName("Account");
                member.setAddress("Tel-Aviv");
                member.setEmail("member@gmail.com");
                member.setUsername("member");
                member.setPassword(passwordEncoder.encode("member"));
                member.setRole(memberSavedRole);
                userRepository.save(member);
            }


            if (!bookRepository.findById(1L).isPresent()) {
                Book book = new Book();
                book.setId(1L);
                book.setAuthor("Kamran");
                book.setName("Science");
                book.setDescription("Science Book description");
                book.setPrice(1000.0);
                book.setStock(10);
                book.setDeleteStatus(false);
                bookRepository.save(book);
            }
        };
    }
}
