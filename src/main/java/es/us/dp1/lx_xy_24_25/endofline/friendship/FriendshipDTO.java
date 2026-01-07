package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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
}
