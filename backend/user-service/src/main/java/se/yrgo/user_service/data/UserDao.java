package se.yrgo.user_service.data;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import se.yrgo.user_service.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    @Query ("SELECT i FROM User i WHERE  i.customerId= ?1")
    Optional<User> getUserByCustomerId(String customerId);

    @Query ("SELECT n FROM User n WHERE n.username = ?1")
    Optional<User> getUserByUsername(String username);

    @Query ("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> getUserByEmail(String email);

    @Query ("SELECT u FROM User u")
    public List<User> listAllUsers();

    @Modifying
    @Transactional
    @Query("Delete FROM User u WHERE u.id = :id")
    public void deleteUserById (long id);


}
