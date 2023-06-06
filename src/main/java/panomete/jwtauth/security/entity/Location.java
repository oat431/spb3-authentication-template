package panomete.jwtauth.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String address;
    String city;
    String state;

    @Builder.Default
    String country = "Thailand";

    String zip;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    Users user;
}
