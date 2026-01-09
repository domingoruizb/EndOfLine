package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PlayerAchievementDTO {

    @JsonProperty("id")
    private Integer id;

    @NotNull
    @JsonProperty("userId")
    private Integer user_id;

    @NotNull
    @JsonProperty("achievementId")
    private Integer achievement_id;

    @JsonProperty("achievedAt")
    private LocalDateTime achieved_at;

    @JsonProperty("userName")
    private String userName;

    public PlayerAchievementDTO () {}

    public PlayerAchievementDTO (PlayerAchievement pa) {
        this.id = pa.getId();
        this.user_id = pa.getUser().getId();
        this.achievement_id = pa.getAchievement().getId();
        this.achieved_at = pa.getAchievedAt();
        this.userName = pa.getUser().getUsername();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
