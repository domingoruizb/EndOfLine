package es.us.dp1.lx_xy_24_25.endofline.user;

import java.time.LocalDate;

import es.us.dp1.lx_xy_24_25.endofline.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends NamedEntity {
	
	@NotNull
	String surname;

	@NotNull
	LocalDate birthdate;

	@NotNull
	String email;

	@NotNull
	@Column(unique = true)
	String username;

	@NotNull
	String password;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

	String avatar;

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