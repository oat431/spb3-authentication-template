package panomete.jwtauth.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import panomete.jwtauth.security.dao.AuthDao;
import panomete.jwtauth.security.entity.Authorities;
import panomete.jwtauth.security.entity.Location;
import panomete.jwtauth.security.entity.Roles;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.payload.request.RegisterRequest;
import panomete.jwtauth.security.payload.request.UpdateRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final AuthDao authDao;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public Users getUserByUsername(String username) {
        return authDao.getUserByUsername(username);
    }

    @Override
    public Users getUserByEmail(String email) {
        return authDao.getUserByEmail(email);
    }

    @Override
    public Users getUserById(UUID id) {
        return authDao.getUserById(id);
    }

    @Override
    public Users createUser(RegisterRequest user) {
        Authorities role = authDao.getAuthorityByName(Roles.ROLE_USER);

        Location location = Location.builder()
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .country(user.getCountry())
                .zip(user.getZip())
                .build();
        authDao.saveLocation(location);

        Users newUser = Users.builder()
                .profilePicture(user.getProfilePicture())
                .username(user.getUsername())
                .platformName(user.getPlatformName())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .tel(user.getTel())
                .birthday(user.getBirthday())
                .location(location)
                .authorities(role)
                .build();
        return authDao.saveUser(newUser);
    }

    @Override
    public Users updateUser(UUID userId,UpdateRequest user) {
        Users oldUser = authDao.getUserById(userId);
        if (oldUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found user id:" + userId);
        }
        Location location = oldUser.getLocation();
        location.setAddress(user.getAddress());
        location.setCity(user.getCity());
        location.setState(user.getState());
        location.setCountry(user.getCountry());
        location.setZip(user.getZip());
        authDao.saveLocation(location);

        oldUser.setProfilePicture(user.getProfilePicture());
        oldUser.setPlatformName(user.getPlatformName());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setTel(user.getTel());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setLocation(location);

        return authDao.saveUser(oldUser);
    }
}
