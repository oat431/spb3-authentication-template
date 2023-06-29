package panomete.jwtauth.users.service;

import panomete.jwtauth.security.entity.Users;

public interface UserService {
    Users getUserByUUID(String uuid);
}
