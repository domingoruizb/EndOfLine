package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // TODO: Accepted or all friendships?
    @GetMapping("/myFriendships")
    @ResponseStatus(HttpStatus.OK)
    public List<Friendship> findAcceptedFriendships() {
        User user = userService.findCurrentUser();
        return friendshipService.getFriendships(user);
    }

    @GetMapping("/myPendingReceivedFriendships")
    @ResponseStatus(HttpStatus.OK)
    public List<Friendship> findPendingReceivedFriendships() {
        User user = userService.findCurrentUser();
        return friendshipService.getPendingFriendships(user);
    }

    @PutMapping("/{id}/acceptFriendship")
    @ResponseStatus(HttpStatus.OK)
    public Friendship acceptFriendship(@PathVariable Integer id) {
        Friendship friendship = friendshipService.getFriendship(id);
        return friendshipService.accept(friendship);
    }

    @DeleteMapping("/{id}/rejectFriendship")
    @ResponseStatus(HttpStatus.OK)
    public void rejectFriendship(@PathVariable Integer id) {
        Friendship friendship = friendshipService.getFriendship(id);
        friendshipService.reject(friendship);
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
        Friendship friendship = friendshipService.getFriendship(id);
        friendshipService.delete(friendship);
    }
}
