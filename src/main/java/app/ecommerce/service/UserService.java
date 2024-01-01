package app.ecommerce.service;

import app.ecommerce.model.Role;
import app.ecommerce.model.User;
import app.ecommerce.repository.RoleRepository;
import app.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder; //It's for password encryption

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /**
     * This function is saving the user in the database.
     *
     * @param user
     * @return
     */
    public User save(User user) {
        Optional<Role> existingRoleCheck = roleRepository.findByName("ROLE_MEMBER");
        if (existingRoleCheck.isPresent()) {
            user.setRole(existingRoleCheck.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            Role role = new Role();
            role.setName("ROLE_MEMBER");
            Role customerRole = roleRepository.save(role);
            user.setRole(customerRole);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);

    }

    /**
     * This function is the key function of spring security.
     * Spring security uses this function to find the user from the database and authenticate it.
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    getAuthority(user.get())
            );
        } else {
            throw new UsernameNotFoundException("User doesn't exists");
        }
    }

    /**
     * This function is providing the role of the user that we have in the datatabase.
     *
     * @param user
     * @return
     */
    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }
}
