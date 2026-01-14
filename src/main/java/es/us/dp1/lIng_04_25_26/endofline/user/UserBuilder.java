package es.us.dp1.lIng_04_25_26.endofline.user;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import java.time.LocalDate;

public class UserBuilder {
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