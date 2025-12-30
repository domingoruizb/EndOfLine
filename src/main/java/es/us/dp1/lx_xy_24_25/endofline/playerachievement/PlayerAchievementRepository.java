package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerAchievementRepository extends CrudRepository<PlayerAchievement, Integer> {

    @Query("SELECT pa FROM PlayerAchievement pa WHERE pa.user.id = ?1")
    PlayerAchievement findByUserId (Integer user_id);

    @Query("SELECT pa FROM PlayerAchievement pa WHERE pa.user.id = ?1")
    List<PlayerAchievement> findAllByUserId (Integer user_id);

    @Query("SELECT CASE WHEN COUNT(pa) > 0 THEN true ELSE false END FROM PlayerAchievement pa WHERE pa.user.id = ?1 AND pa.achievement.id = ?2")
    boolean existsByUserIdAndAchievementId(Integer userId, Integer achievementId);

    @Query("SELECT pa FROM PlayerAchievement pa WHERE pa.achievement.id = ?1")
    List<PlayerAchievement> findAllByAchievementId(Integer achievementId);

}
