package es.us.dp1.lIng_04_25_26.endofline.card;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends BaseEntity {

	@Min(0)
	@Max(5)
	private Integer initiative;

	@NotNull
	private Color color;

	@NotNull
	private String image;

}
