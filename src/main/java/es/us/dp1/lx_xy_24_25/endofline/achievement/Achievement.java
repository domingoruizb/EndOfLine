package es.us.dp1.lx_xy_24_25.endofline.achievement;

import es.us.dp1.lx_xy_24_25.endofline.model.NamedEntity;
import es.us.dp1.lx_xy_24_25.endofline.playerachievement.PlayerAchievement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    Category category;

    @OneToMany(mappedBy = "achievement")
    Set<PlayerAchievement> playerAchievements;

}
