package es.us.dp1.lIng_04_25_26.endofline.achievement;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import es.us.dp1.lIng_04_25_26.endofline.enums.Category;
import es.us.dp1.lIng_04_25_26.endofline.model.NamedEntity;
import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievement;

@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement extends NamedEntity {

    @NotBlank
    private String description;

    private String badgeImage;

    @Min(0)
    private double threshold;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @OneToMany(mappedBy = "achievement")
    private Set<PlayerAchievement> playerAchievements;

}
