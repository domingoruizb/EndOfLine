package es.us.dp1.lIng_04_25_26.endofline.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.us.dp1.lIng_04_25_26.endofline.authentication.payload.request.SignupRequest;
import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Column(unique = true)
    String email;

    @NotNull
    @Column(unique = true)
    String username;

    @NotNull
    String password;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "authority")
    Authority authority;

    String avatar;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<GamePlayer> gamePlayers = new ArrayList<>();


    public static User build(SignupRequest request, String password, Authority authority) {
        return new UserBuilder()
            .username(request.getUsername())
            .password(password)
            .authority(authority)
            .avatar(request.getAvatar())
            .email(request.getEmail())
            .birthdate(LocalDate.parse(request.getBirthdate()))
            .name(request.getName())
            .surname(request.getSurname())
            .build();
    }

}

