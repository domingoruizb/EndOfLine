package es.us.dp1.lx_xy_24_25.endofline.achievement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class AchievementService {

    AchievementRepository repo;

    @Autowired
    public AchievementService(AchievementRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    List<Achievement> getAchievements() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Achievement getById(int id) {
        Optional<Achievement> result = repo.findById(id);
        return result.orElseThrow(() -> new es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException("Achievement with id " + id + " not found"));
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return repo.save(newAchievement);
    }

    @Transactional
    public Achievement updateAchievement(Integer id, Achievement achievement) {
        Achievement achievementToUpdate = getById(id);
        BeanUtils.copyProperties(achievement, achievementToUpdate, "id");
        return repo.save(achievementToUpdate);
    }

    @Transactional
    public void deleteAchievementById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementByName(String name) {
        return repo.findByName(name);
    }

}
