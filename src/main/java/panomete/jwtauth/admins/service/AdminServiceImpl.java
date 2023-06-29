package panomete.jwtauth.admins.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import panomete.jwtauth.security.dao.AuthDao;
import panomete.jwtauth.security.entity.Users;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    final AuthDao authDao;
    @Override
    public Users enableAccount(String uuid) {
        Users user = authDao.getUserById(UUID.fromString(uuid));
        user.setEnables(!user.getEnables());
        return authDao.saveUser(user);
    }

    @Override
    public Users deleteAccount(String uuid) {
        return authDao.deleteUser(authDao.getUserById(UUID.fromString(uuid)));
    }
}
