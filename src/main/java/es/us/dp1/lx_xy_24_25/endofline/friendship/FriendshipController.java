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
    public Iterable<Friendship> findAcceptedFriendships() {
        Integer id = userService.findCurrentUser().getId();
        return friendshipService.findFriendshipsOf(id);
    }

    @GetMapping("/myPendingReceivedFriendships")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Friendship> findPendingReceivedFriendships() {
        Integer id = userService.findCurrentUser().getId();
        return friendshipService.findPendingReceivedFriendships(id);
    }

    @PutMapping("/{id}/acceptFriendship")
    @ResponseStatus(HttpStatus.OK)
    public Friendship acceptFriendship(@PathVariable Integer id) {
        Friendship friendship = friendshipService.findById(id);
        return friendshipService.acceptFriendShip(friendship);
    }

    @DeleteMapping("/{id}/rejectFriendship")
    @ResponseStatus(HttpStatus.OK)
    public void rejectFriendship(@PathVariable Integer id) {
        Friendship friendship = friendshipService.findById(id);
        friendshipService.rejectFriendShip(friendship);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Friendship create(@RequestBody @Valid FriendshipDTO friendshipDTO) {
        User receiver = userService.findUser(friendshipDTO.getReceiver());
        return friendshipService.create(receiver);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        Friendship friendship = friendshipService.findById(id);
        friendshipService.delete(friendship);
    }
}
