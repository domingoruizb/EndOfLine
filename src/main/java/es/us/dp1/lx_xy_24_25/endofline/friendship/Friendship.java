package es.us.dp1.lx_xy_24_25.endofline.friendship;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;

@Getter
@Setter
@Entity
@Table(name = "friendships")
public class Friendship extends BaseEntity {

    @JsonManagedReference("sentFriendships")
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User sender;

    @JsonManagedReference("receivedFriendships")
    @ManyToOne
    @NotNull
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User receiver;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "friend_state")
    FriendStatus friendState;

    public static Friendship build (
        User sender,
        User receiver,
        FriendStatus friendState
    ) {
        Friendship friendship = new Friendship();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setFriendState(friendState);
        return friendship;
    }

}
