package sahachan.template.auth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sahachan.template.auth.security.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
