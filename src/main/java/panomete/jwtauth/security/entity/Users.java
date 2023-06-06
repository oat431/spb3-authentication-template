package panomete.jwtauth.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String profilePicture;
    String platformName;
    String username;
    String firstName;
    String lastName;
    String tel;
    String password;
    Date birthday;

    @Email
    String email;

    @OneToOne
    Location location;

    public Integer getAge() {
        return LocalDate.now().getYear() - birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    }

}
