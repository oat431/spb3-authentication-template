package sahachan.template.auth.security.service;


import sahachan.template.auth.security.entity.User;

public interface UserService {
    User findByUsername(String username);
    User addUser(User user);
    User updateUser(Long id, User user);
    Boolean deleteUser(Long id);
    User updatePassword(Long id,String newPassword);
    User updateEmail(Long id,String newEmail);
    User getUser(Long id);
    Boolean checkEmail(String email);
    Boolean checkUsername(String username);
}
