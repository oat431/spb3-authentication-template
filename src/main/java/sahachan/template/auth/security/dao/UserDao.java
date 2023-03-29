package sahachan.template.auth.security.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import sahachan.template.auth.security.entity.User;

import java.util.List;

public interface UserDao {
    User findByUsername(String username);

    User getUser(Long id);
    User addUser(User user);
    User updateUser(User user);
    Boolean deleteUser(Long id);
    List<User> getAllUser();
    Page<User> getAllUser(PageRequest pageRequest);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
}
