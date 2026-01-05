package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friendships")
@Tag(name = "Friendship", description = "API for the management of Friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;

    @Autowired
    public FriendshipController (
        FriendshipService friendshipService,
        UserService userService
    ) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    @GetMapping("/myFriendships")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Friendship> findAcceptedFriendships() throws Exception {
        Integer id = userService.findCurrentUser().getId();
        return friendshipService.findFriendshipsOf(id);
    }

    @GetMapping("/myPendingReceivedFriendships")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Friendship> findPendingReceivedFriendships() throws Exception {
        Integer id = userService.findCurrentUser().getId();
        return friendshipService.findPendingReceivedFriendships(id);
    }

    @PutMapping("/{id}/acceptFriendship")
    @ResponseStatus(HttpStatus.OK)
    public Friendship acceptFriendship(@PathVariable Integer id) {
        return friendshipService.acceptFriendShip(id);
    }

    @DeleteMapping("/{id}/rejectFriendship")
    @ResponseStatus(HttpStatus.OK)
    public void rejectFriendship(@PathVariable Integer id) {
        friendshipService.rejectFriendShip(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Friendship create(@RequestBody @Valid FriendshipDTO friendshipDTO) {
        return friendshipService.create(friendshipDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        Friendship friendship = friendshipService.findById(id);
        User currentUser = userService.findCurrentUser();
        if (currentUser.equals(friendship.getSender()) || currentUser.equals(friendship.getReceiver())) {
            friendshipService.delete(id);
        } else {
            throw new AccessDeniedException("You are not authorized to delete this friendship.");
        }
    }
}
