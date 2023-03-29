package sahachan.template.auth.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sahachan.template.auth.security.dao.AuthorityDao;
import sahachan.template.auth.security.dao.LocationDao;
import sahachan.template.auth.security.dao.UserDao;
import sahachan.template.auth.security.entity.Authority;
import sahachan.template.auth.security.entity.AuthorityName;
import sahachan.template.auth.security.entity.Location;
import sahachan.template.auth.security.entity.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final UserDao userDao;
    final AuthorityDao authorityDao;
    final LocationDao locationDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User addUser(User user) {
        Authority userRole = Authority.builder().name(AuthorityName.ROLE_USER).build();
        authorityDao.addAuthority(userRole);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        locationDao.addLocation(user.getLocation());

        User newUser = User.builder()
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .location(user.getLocation())
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
        newUser.getAuthorities().add(userRole);
        return userDao.addUser(newUser);
    }

    @Override
    public User updateUser(Long id, User user) {
        User oldUser = userDao.getUser(id);
        Location location = user.getLocation();
        locationDao.addLocation(location);
        oldUser.setLocation(location);
        oldUser.setFirstname(user.getFirstname());
        oldUser.setLastname(user.getLastname());
        oldUser.setBirthday(user.getBirthday());
        return userDao.updateUser(oldUser);
    }

    @Override
    public Boolean deleteUser(Long id) {
        return userDao.deleteUser(id);
    }

    @Override
    public User updatePassword(Long id, String newPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User oldUser = userDao.getUser(id);
        oldUser.setPassword(encoder.encode(newPassword));
        return userDao.updateUser(oldUser);
    }

    @Override
    public User updateEmail(Long id, String newEmail) {
        User oldUser = userDao.getUser(id);
        oldUser.setEmail(newEmail);
        oldUser.setUsername(newEmail);
        return userDao.updateUser(oldUser);
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    public Boolean checkEmail(String email) {
        return userDao.getUserByEmail(email) != null;
    }

    @Override
    public Boolean checkUsername(String username) {
        return userDao.getUserByUsername(username) != null;
    }
}
