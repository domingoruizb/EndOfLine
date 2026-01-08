package es.us.dp1.lIng_04_25_26.endofline.friendship;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import es.us.dp1.lIng_04_25_26.endofline.enums.FriendStatus;

@Getter
@Setter
public class FriendshipDTO {
    @Id
    Integer id;

    @NotNull
    Integer receiver;

    @Enumerated(EnumType.STRING)
    FriendStatus friendship_state;

    public FriendshipDTO(Integer receiverId) {
        this.receiver = receiverId;
    }
    public FriendshipDTO() {
    }
}
