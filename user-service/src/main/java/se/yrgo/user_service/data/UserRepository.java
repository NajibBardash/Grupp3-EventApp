package se.yrgo.user_service.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.yrgo.user_service.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

    @Query ("SELECT i FROM User i WHERE  i.customerId= ?1")
    public User findByCustomerId(String customerId);

    @Query ("SELECT n FROM User n WHERE n.name = ?1")
    public User findByName(String name);


}
