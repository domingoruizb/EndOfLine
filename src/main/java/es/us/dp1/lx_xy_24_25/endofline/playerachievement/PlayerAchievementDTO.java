package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PlayerAchievementDTO {

    @JsonProperty("id")
    private Integer id;

    @NotNull
    @JsonProperty("user_id")
    private Integer user_id;

    @NotNull
    @JsonProperty("achievement_id")
    private Integer achievement_id;

    @JsonProperty("achieved_at")
    private LocalDateTime achieved_at;

    public PlayerAchievementDTO () {}

    public PlayerAchievementDTO (PlayerAchievement pa) {
        this.id = pa.getId();
        this.user_id = pa.getUser().getId();
        this.achievement_id = pa.getAchievement().getId();
        this.achieved_at = pa.getAchievedAt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public LocalDateTime getAchieved_at() {
        return achieved_at;
    }

    public void setAchieved_at(LocalDateTime achieved_at) {
        this.achieved_at = achieved_at;
    }
}
