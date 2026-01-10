package es.us.dp1.lIng_04_25_26.endofline.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AchievementDTO {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Integer threshold;
    private boolean unlocked;
    private String badgeImage;

    public static AchievementDTO build (
        Achievement dto,
        Boolean unlocked
    ) {
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setId(dto.getId());
        achievementDTO.setName(dto.getName());
        achievementDTO.setDescription(dto.getDescription());
        achievementDTO.setCategory(dto.getCategory().toString());
        achievementDTO.setThreshold((int) dto.getThreshold());
        achievementDTO.setUnlocked(unlocked);
        achievementDTO.setBadgeImage(dto.getBadgeImage());
        return achievementDTO;
    }

}
