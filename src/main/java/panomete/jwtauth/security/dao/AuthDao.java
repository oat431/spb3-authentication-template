package panomete.jwtauth.security.dao;

import panomete.jwtauth.security.entity.Authorities;
import panomete.jwtauth.security.entity.Location;
import panomete.jwtauth.security.entity.Roles;
import panomete.jwtauth.security.entity.Users;

import java.util.UUID;

public interface AuthDao {
    Users getUserByUsername(String username);
    Users getUserByEmail(String email);
    Users getUserById(UUID id);
    Users saveUser(Users user);

    Users deleteUser(Users user);

    Authorities saveAuthority(Authorities authority);
    Authorities getAuthorityByName(Roles name);

    Location saveLocation(Location location);
}
