package panomete.jwtauth.security.service;

import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.payload.request.RegisterRequest;
import panomete.jwtauth.security.payload.request.UpdateRequest;

import java.util.UUID;

public interface AuthService {
    Users getUserByUsername(String username);
    Users getUserByEmail(String email);
    Users getUserById(UUID id);
    Users createUser(RegisterRequest user);
    Users updateUser(UUID userId, UpdateRequest user);
}
