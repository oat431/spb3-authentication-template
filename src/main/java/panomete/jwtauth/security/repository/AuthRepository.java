package panomete.jwtauth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import panomete.jwtauth.security.entity.Users;

public interface AuthRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
