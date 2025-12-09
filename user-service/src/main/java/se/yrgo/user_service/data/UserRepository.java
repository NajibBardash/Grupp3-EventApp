package se.yrgo.user_service.data;
import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.user_service.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

}
