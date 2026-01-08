package es.us.dp1.lIng_04_25_26.endofline.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(Integer id);

	Iterable<User> findAll();

	@Query("SELECT u FROM User u WHERE u.authority.type = :authorityType")
	Iterable<User> findAllByAuthorityType(String authorityType);


}
