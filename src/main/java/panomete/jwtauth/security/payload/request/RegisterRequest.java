package panomete.jwtauth.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String birthday;
    String email;
    String address;
    String city;
    String state;
    String country;
    String zip;
}
