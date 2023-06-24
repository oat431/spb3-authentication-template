package panomete.jwtauth.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String profilePicture;
    String platformName;

    @Column(unique = true)
    String username;

    String firstName;
    String lastName;

    @Column(unique = true)
    String tel;

    String password;
    Date birthday;

    @Builder.Default
    Date lastPasswordReset = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    @Builder.Default
    Boolean enables = true;

    @Email
    @Column(unique = true)
    String email;

    @OneToOne
    Location location;

    @ManyToOne
    Authorities authorities;

    public Integer getAge() {
        return LocalDate.now().getYear() - birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    }

    public String getUserId() {
        return id.toString();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.authorities.getName().name()));
    }

    public String getSimpleAuthorities() {
        return this.authorities.getName().name();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enables;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enables;
    }

}
