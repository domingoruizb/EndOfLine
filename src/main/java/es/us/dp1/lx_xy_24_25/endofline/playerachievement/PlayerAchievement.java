package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "playerachievements")
public class PlayerAchievement extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement_id")
    @NotNull
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Achievement achievement;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

}
