package panomete.jwtauth.security.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.repository.AuthRepository;

@Repository
@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    final AuthRepository authRepository;
    @Override
    public Users getUserByUsername(String username) {
        return authRepository.findByUsername(username);
    }
}
