package panomete.jwtauth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import panomete.jwtauth.security.entity.Authorities;
import panomete.jwtauth.security.entity.Roles;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
    Authorities findByName(Roles name);
}
