package panomete.jwtauth.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    String id;
    String profilePicture;
    String platformName;
    String username;
    String name;
    String tel;
    Integer age;
    String email;
    LocationDto location;
    String role;
}
