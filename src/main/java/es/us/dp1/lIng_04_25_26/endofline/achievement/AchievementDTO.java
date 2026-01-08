package es.us.dp1.lIng_04_25_26.endofline.achievement;

public class AchievementDTO {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Integer threshold;
    private boolean unlocked;
    private String badgeImage;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }

    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }

    public String getBadgeImage() { return badgeImage; }
    public void setBadgeImage(String badgeImage) { this.badgeImage = badgeImage; }
}
