package panomete.jwtauth.security.service;

import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.payload.request.RegisterRequest;
import panomete.jwtauth.security.payload.request.UpdateRequest;

public interface AuthService {
    Users getUserByUsername(String username);
    Users getUserByEmail(String email);
    Users getUserById(Long id);
    Users createUser(RegisterRequest user);
    Users updateUser(Long userId, UpdateRequest user);
}
