package es.us.dp1.lx_xy_24_25.endofline.user;

import java.time.LocalDate;
import java.util.Set;

import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.model.NamedEntity;
import es.us.dp1.lx_xy_24_25.endofline.playerachievement.PlayerAchievement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appusers")
public class User extends NamedEntity {

	@NotNull
	String name;

	@NotNull
	String surname;

	@NotNull
	LocalDate birthdate;

	@NotNull
	String email;

	@Column(unique = true)
	String username;

	@NotNull
	String password;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

    @OneToMany(mappedBy = "user")
    Set<PlayerAchievement> achievements;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}

}
