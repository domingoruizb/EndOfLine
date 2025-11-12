package es.us.dp1.lx_xy_24_25.endofline.card;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends BaseEntity {

	@Min(0)
	@Max(5)
	private Integer initiative;

	private LocalDateTime updatedAt;

	@NotNull
	private Color color;

	@NotNull
	private String image;
	
}
