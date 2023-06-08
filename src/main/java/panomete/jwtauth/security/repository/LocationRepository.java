package panomete.jwtauth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import panomete.jwtauth.security.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
