package panomete.jwtauth.security;

import panomete.jwtauth.security.entity.Users;

import java.sql.Date;
import java.util.UUID;

public class UserMock {
    Users getMockUser1(){
        return Users.builder()
                .id(UUID.fromString("c8d67030-c67b-4139-a45a-1402ef814e5b"))
                .profilePicture("profilePicture")
                .platformName("platformName")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .tel("tel")
                .password("password")
                .birthday(Date.valueOf("2000-11-09"))
                .lastPasswordReset(null)
                .enables(true)
                .email("email@email.com")
                .location(null)
                .authorities(null)
                .build();
    }
}
