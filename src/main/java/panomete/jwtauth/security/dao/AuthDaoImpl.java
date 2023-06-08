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

    @Override
    public Users getUserByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    @Override
    public Users getUserById(Long id) {
        return authRepository.findById(id).orElse(null);
    }

    @Override
    public Users saveUser(Users user) {
        return authRepository.save(user);
    }
}
