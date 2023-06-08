package panomete.jwtauth.security.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import panomete.jwtauth.security.entity.Authorities;
import panomete.jwtauth.security.entity.Location;
import panomete.jwtauth.security.entity.Roles;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.repository.AuthRepository;
import panomete.jwtauth.security.repository.AuthoritiesRepository;
import panomete.jwtauth.security.repository.LocationRepository;

@Repository
@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    final AuthRepository authRepository;
    final AuthoritiesRepository authoritiesRepository;
    final LocationRepository locationRepository;
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

    @Override
    public Authorities saveAuthority(Authorities authority) {
        return authoritiesRepository.save(authority);
    }

    @Override
    public Authorities getAuthorityByName(Roles name) {
        return authoritiesRepository.findByName(name);
    }

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
