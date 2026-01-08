package es.us.dp1.lx_xy_24_25.endofline.achievement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
}
