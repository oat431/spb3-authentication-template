package panomete.jwtauth.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.service.AuthService;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    AuthService authService;
    private final UserMock userMock = new UserMock();

    @Test
    @DisplayName("Get user by id test")
    void getUserByIdTestNormalCase() {
        UUID id = UUID.fromString("c8d67030-c67b-4139-a45a-1402ef814e5b");
        when(authService.getUserById(id)).thenReturn(userMock.getMockUser1());
        Users user = authService.getUserById(id);
        assert user.getId().equals(id);
        assert user.getUsername().equals("username");
        assert user.getFirstName().equals("firstName");
        assert user.getLastName().equals("lastName");
    }

    @Test
    @DisplayName("Get user by id test but user is null")
    void getUserByIdTestButUserIsNull() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
        when(authService.getUserById(id)).thenReturn(null);
        Users user = authService.getUserById(id);
        assert user == null;
    }

    @Test
    @DisplayName("Get user by username test")
    void getUserByUsernameTestNormalCase() {
        String username = "username";
        when(authService.getUserByUsername(username)).thenReturn(userMock.getMockUser1());
        Users user = authService.getUserByUsername(username);
        assert user.getUsername().equals(username);
        assert user.getFirstName().equals("firstName");
        assert user.getLastName().equals("lastName");
    }

    @Test
    @DisplayName("Get user by username test but user is null")
    void getUserByUsernameTestButUserIsNull() {
        String username = "username";
        when(authService.getUserByUsername(username)).thenReturn(null);
        Users user = authService.getUserByUsername(username);
        assert user == null;
    }
}
