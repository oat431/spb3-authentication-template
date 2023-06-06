package panomete.jwtauth.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_seq")
    @SequenceGenerator(name="authorities_seq", sequenceName = "authorities_seq", allocationSize = 1)
    Long id;

    @Enumerated(EnumType.STRING)
    Roles name;

    @OneToMany(mappedBy = "authorities")
    List<Users> user;
}
