package es.us.dp1.lIng_04_25_26.endofline.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(Integer id);

	@Query("""
        SELECT u FROM User u WHERE u.
            id <> :userId
        ORDER BY
            CASE WHEN u.authority.type = 'ADMIN' THEN 0 ELSE 1 END,
            u.username ASC
    """)
    Page<User> findAllExceptMySelf(Integer userId, Pageable pageable);

}
