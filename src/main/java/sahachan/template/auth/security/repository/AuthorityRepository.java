package sahachan.template.auth.security.repository;



import org.springframework.data.repository.CrudRepository;
import sahachan.template.auth.security.entity.Authority;
import sahachan.template.auth.security.entity.AuthorityName;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findByName(AuthorityName input);
}
