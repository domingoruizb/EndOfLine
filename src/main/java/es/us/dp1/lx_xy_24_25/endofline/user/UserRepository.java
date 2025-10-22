package es.us.dp1.lx_xy_24_25.endofline.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsUserById(Integer id);

	Optional<User> findById(Integer id);

	@Query("SELECT u FROM User u WHERE u.authority.authority = :auth")
	Iterable<User> findAllByAuthority(String auth);


}
