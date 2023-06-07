package panomete.jwtauth.security.dao;

import org.springframework.stereotype.Repository;
import panomete.jwtauth.security.entity.Users;

public interface AuthDao {
    Users getUserByUsername(String username);
}
