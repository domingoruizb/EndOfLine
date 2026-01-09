package es.us.dp1.lIng_04_25_26.endofline.user;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String email;
    private String username;
    private Authority authority;
    private String avatar;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.birthdate = user.getBirthdate();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.authority = user.getAuthority();
        this.avatar = user.getAvatar();
    }
}
