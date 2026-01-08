package es.us.dp1.lIng_04_25_26.endofline.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Integer>{

	@Query("SELECT DISTINCT auth FROM Authority auth WHERE auth.type LIKE :type%")
	Optional<Authority> findByType(String type);

}
