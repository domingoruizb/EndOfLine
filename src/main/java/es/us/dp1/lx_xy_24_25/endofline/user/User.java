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

}

