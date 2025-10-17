package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerAchievementService {

    PlayerAchievementRepository repository;

    @Autowired
    public PlayerAchievementService(PlayerAchievementRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Iterable<PlayerAchievement> findAll() {
        return repository.findAll();
    }

}
