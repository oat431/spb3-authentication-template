package panomete.jwtauth.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import panomete.jwtauth.security.dao.AuthDao;
import panomete.jwtauth.security.entity.Users;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final AuthDao authDao;
    @Override
    public Users getUserByUUID(String uuid) {
        return authDao.getUserById(UUID.fromString(uuid));
    }
}
