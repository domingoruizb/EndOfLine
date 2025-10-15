package es.us.dp1.lx_xy_24_25.endofline.statistic;

import es.us.dp1.lx_xy_24_25.endofline.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

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
    Metric metric;

    public String getActualDescription() {
        return description.replace("<THRESHOLD>", String.valueOf(threshold));
    }

}
