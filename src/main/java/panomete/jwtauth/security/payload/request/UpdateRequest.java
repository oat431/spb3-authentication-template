package panomete.jwtauth.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    String profilePicture;
    String platformName;
    String firstName;
    String lastName;
    String tel;
    String birthday;
    String address;
    String city;
    String state;
    String country;
    String zip;
}
