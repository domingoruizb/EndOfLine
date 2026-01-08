package es.us.dp1.lIng_04_25_26.endofline.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.enums.FriendStatus;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.AccessDeniedException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    @Autowired
    public FriendshipService(
        FriendshipRepository friendshipRepository,
        UserService userService
    ) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Friendship getFriendship(Integer id) {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new FriendshipNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Friendship> getFriendships(User user) {
        return friendshipRepository.findFriendships(user.getId());
    }

    @Transactional(readOnly = true)
    public List<Friendship> getPendingFriendships(User user) {
        return friendshipRepository.findPending(user.getId());
    }

    private Boolean check(User sender, User receiver) {
        if (sender.getId().equals(receiver.getId())) {
            throw new FriendshipBadRequestException("You cannot be friends with yourself");
        }

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendship(sender.getId(), receiver.getId());
        if (!optionalFriendship.isPresent()) {
            return true;
        }

        FriendStatus friendStatus = optionalFriendship.get().getFriendState();
        switch (friendStatus) {
            case PENDING:
                throw new FriendshipBadRequestException("There is already a pending friendship request with this user");
            default:
                throw new FriendshipBadRequestException("You are already friends with this user");
        }
    }

    @Transactional
    public Friendship create(User receiver) {
        User sender = userService.findCurrentUser();
        check(sender, receiver);
        Friendship newFriendship = Friendship.build(sender, receiver, FriendStatus.PENDING);
        return friendshipRepository.save(newFriendship);
    }

    @Transactional
    public Friendship accept(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (friendship.getFriendState() != FriendStatus.PENDING) {
            throw new FriendshipBadRequestException("The friendship has already been accepted");
        }

        if (!currentUser.getId().equals(friendship.getReceiver().getId())) {
            throw new FriendshipForbiddenException("Only the receiver can accept the friendship");
        }

        friendship.setFriendState(FriendStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void reject(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (!currentUser.getId().equals(friendship.getReceiver().getId())) {
            throw new FriendshipForbiddenException("Only the receiver can reject the friendship");
        }
        friendshipRepository.delete(friendship);
    }


    @Transactional
    public Friendship update(Integer id, FriendshipDTO friendshipDTO) {
        Friendship friendshipToUpdate = getFriendship(id);
        friendshipToUpdate.setFriendState(friendshipDTO.getFriendship_state());
        return friendshipRepository.save(friendshipToUpdate);
    }

    @Transactional
    public void delete(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (!currentUser.equals(friendship.getSender()) && !currentUser.equals(friendship.getReceiver())) {
            throw new FriendshipForbiddenException("You are not authorized to delete this friendship.");
        }

        friendshipRepository.deleteById(friendship.getId());
    }
}
