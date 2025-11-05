package es.us.dp1.lx_xy_24_25.endofline.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.model.NamedEntity;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<GamePlayer> gamePlayer = new ArrayList<>();

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


	// setter for the collection used by Hibernate with orphanRemoval = true
	// it is important to mutate the existing collection instance (clear and add)
	// instead of replacing the reference, so Hibernate can track orphan removals properly

	public void setGamePlayer(List<GamePlayer> gamePlayers) {
        if (this.gamePlayer == null) {
            this.gamePlayer = new ArrayList<>();
        } else {
            this.gamePlayer.clear();
        }

        if (gamePlayers != null) {
            for (GamePlayer gp : gamePlayers) {
                if (gp != null) {
                    gp.setUser(this);
                }
            }
            this.gamePlayer.addAll(gamePlayers);
        }
    }

}

