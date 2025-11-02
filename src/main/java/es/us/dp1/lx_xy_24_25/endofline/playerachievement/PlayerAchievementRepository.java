package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAchievementRepository extends CrudRepository<PlayerAchievement, Integer> {

    @Query("SELECT pa FROM PlayerAchievement pa WHERE pa.user.id = ?1")
    PlayerAchievement findByUserId (Integer user_id);

}
