package panomete.jwtauth.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String profilePicture;
    String platformName;
    String username;
    String firstName;
    String lastName;
    String tel;
    String password;
    Date birthday;
    String email;
    String address;
    String city;
    String state;
    String country;
    String zip;
}
