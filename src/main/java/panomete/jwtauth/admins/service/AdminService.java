package panomete.jwtauth.admins.service;

import panomete.jwtauth.security.entity.Users;

public interface AdminService {
    Users enableAccount(String uuid);
    Users deleteAccount(String uuid);
}
