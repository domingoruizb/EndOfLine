package es.us.dp1.lx_xy_24_25.endofline.friendship;

import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.BadRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.user.UserRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@Service
public class FriendshipService {

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserService userService;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Transactional(readOnly = true)
    public Friendship findById(Integer id) throws DataAccessException {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship", "id", id));
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findAll() throws DataAccessException {
        return friendshipRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findFriendshipsOf(Integer id) throws DataAccessException {
        return friendshipRepository.findFriendshipsByUserId(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findReceivedIterableFriendships(Integer id) throws DataAccessException {
        return friendshipRepository.findFriendshipsByUserId(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findPendingReceivedFriendships(@Param("userId") Integer userId){
        return friendshipRepository.findPendingReceivedFriendships(userId);
    }

    @Transactional(readOnly = true)
    public Iterable<Friendship> findFriendshipsByUserId(Integer id, FriendStatus friendState)
            throws DataAccessException {
        Iterable<Friendship> friendships = friendshipRepository.findFriendshipsByUserId(id);
        return StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> friendship.getFriendState().equals(friendState))
                .collect(Collectors.toList());
    }

    private Boolean checkFriendship(Integer sender_id, Integer receiver_id) throws DataAccessException {
        if (sender_id.equals(receiver_id))
            throw new BadRequestException("You cannot create a friendship with yourself.");

        if (!userService.existsUser(sender_id) || !userService.existsUser(receiver_id))
            throw new BadRequestException("User with id " + sender_id + " or " + receiver_id + " does not exist.");

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySenderAndReceiver(sender_id,
                receiver_id);
        if (optionalFriendship.isPresent()) {
            FriendStatus friendStatus = optionalFriendship.get().getFriendState();
            switch (friendStatus) {
                case PENDING:
                    throw new BadRequestException("There is already a pending friendship request with this user.");
                default:
                    throw new BadRequestException("You are already friends with this user.");
            }
        }
        return true;
    }

    @Transactional
    public Friendship create(FriendshipDTO friendshipDTO) throws DataAccessException {
        checkFriendship(friendshipDTO.sender, friendshipDTO.receiver);
        userService.existsUser(friendshipDTO.sender);
        userService.existsUser(friendshipDTO.receiver);
        Friendship newFriendship = new Friendship();
        newFriendship.setSender(userService.findUser(friendshipDTO.sender));
        newFriendship.setReceiver(userService.findUser(friendshipDTO.receiver));
        newFriendship.setFriendState(FriendStatus.PENDING);
        return friendshipRepository.save(newFriendship);
    }

    @Transactional
    public Friendship acceptFriendShip(Integer id) throws DataAccessException {
        Friendship friendshipToAccept = findById(id);
        User currentUser = userService.findCurrentUser();
        if (friendshipToAccept.getFriendState() != FriendStatus.PENDING) {
            throw new BadRequestException("The friendship has already been accepted.");
        }

        if (!currentUser.getId().equals(friendshipToAccept.getReceiver().getId())) {
            throw new AccessDeniedException("Only the receiver can accept the friendship.");
        }

        friendshipToAccept.setFriendState(FriendStatus.ACCEPTED);
        return friendshipRepository.save(friendshipToAccept);

    }

    @Transactional
    public void rejectFriendShip(Integer id) throws DataAccessException {
        Friendship friendshipToReject = findById(id);
        User currentUser = userService.findCurrentUser();
        if (!currentUser.getId().equals(friendshipToReject.getReceiver().getId())) {
            throw new AccessDeniedException("Only the receiver can reject the friendship.");
        }
        friendshipRepository.delete(friendshipToReject);
    }


    @Transactional
    public Friendship update(Integer id, FriendshipDTO friendshipDTO) throws DataAccessException {
        Friendship friendshipToUpdate = findById(id);
        friendshipToUpdate.setFriendState(friendshipDTO.getFriendship_state());
        return friendshipRepository.save(friendshipToUpdate);
    }

    @Transactional
    public void delete(Integer id) throws DataAccessException {
            friendshipRepository.deleteById(id);
    }
}
