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


    public static class UserBuilder {
        private String surname;
        private LocalDate birthdate;
        private String email;
        private String username;
        private String password;
        private Authority authority;
        private String avatar;
        private String name;

        public UserBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder authority(Authority authority) {
            this.authority = authority;
            return this;
        }

        public UserBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public User build() {
            User user = new User();
            user.setSurname(this.surname);
            user.setBirthdate(this.birthdate);
            user.setEmail(this.email);
            user.setUsername(this.username);
            user.setPassword(this.password);
            user.setAuthority(this.authority);
            user.setAvatar(this.avatar);
            user.setName(this.name);
            return user;
        }
    }

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

