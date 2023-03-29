package sahachan.template.auth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sahachan.template.auth.security.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
