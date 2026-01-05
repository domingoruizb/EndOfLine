package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.BadRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.friendship.FriendshipNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.friendship.FriendshipNotValidException;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Friendship findById(Integer id) {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new FriendshipNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findFriendshipsOf(Integer id) {
        return friendshipRepository.findFriendshipsByUserId(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findPendingReceivedFriendships(@Param("userId") Integer userId) {
        return friendshipRepository.findPendingReceivedFriendships(userId);
    }

    private Boolean checkFriendship(User sender, User receiver) {
        if (sender.getId().equals(receiver.getId())) {
            throw new FriendshipNotValidException(sender, receiver);
        }

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySenderAndReceiver(sender.getId(), receiver.getId());
        if (!optionalFriendship.isPresent()) {
            return true;
        }

        FriendStatus friendStatus = optionalFriendship.get().getFriendState();
        switch (friendStatus) {
            case PENDING:
                throw new FriendshipNotValidException("There is already a pending friendship request with this user");
            default:
                throw new FriendshipNotValidException("You are already friends with this user");
        }
    }

    @Transactional
    public Friendship create(User receiver) {
        User sender = userService.findCurrentUser();

        checkFriendship(sender, receiver);

        Friendship newFriendship = new Friendship();
        newFriendship.setSender(sender);
        newFriendship.setReceiver(receiver);
        newFriendship.setFriendState(FriendStatus.PENDING);

        return friendshipRepository.save(newFriendship);
    }

    @Transactional
    public Friendship acceptFriendShip(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (friendship.getFriendState() != FriendStatus.PENDING) {
            throw new FriendshipNotValidException("The friendship has already been accepted");
        }

        if (!currentUser.getId().equals(friendship.getReceiver().getId())) {
            throw new FriendshipNotValidException("Only the receiver can accept the friendship");
        }

        friendship.setFriendState(FriendStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendShip(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (!currentUser.getId().equals(friendship.getReceiver().getId())) {
            throw new FriendshipNotValidException("Only the receiver can reject the friendship");
        }
        friendshipRepository.delete(friendship);
    }


    @Transactional
    public Friendship update(Integer id, FriendshipDTO friendshipDTO) {
        Friendship friendshipToUpdate = findById(id);
        friendshipToUpdate.setFriendState(friendshipDTO.getFriendship_state());
        return friendshipRepository.save(friendshipToUpdate);
    }

    @Transactional
    public void delete(Friendship friendship) {
        User currentUser = userService.findCurrentUser();
        if (currentUser.equals(friendship.getSender()) || currentUser.equals(friendship.getReceiver())) {
            friendshipRepository.deleteById(friendship.getId());
        } else {
            throw new AccessDeniedException("You are not authorized to delete this friendship.");
        }
    }
}
