package es.us.dp1.lx_xy_24_25.endofline.friendship;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.BadRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.user.UserRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@Service
public class FriendshipService {

    FriendshipRepository friendshipRepository;

    UserRepository userRepository;

    UserService userService;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
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

        if (!userRepository.existsUserById(sender_id) || !userRepository.existsUserById(receiver_id))
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
    public Friendship create(Integer sender_id, Integer receiver_id) throws DataAccessException {
        checkFriendship(sender_id, receiver_id);
        userService.existsUser(sender_id);
        userService.existsUser(receiver_id);
        Friendship newFriendship = new Friendship();
        newFriendship.setSender(userService.findUser(sender_id));
        newFriendship.setReceiver(userService.findUser(receiver_id));
        newFriendship.setFriendState(FriendStatus.PENDING);
        return friendshipRepository.save(newFriendship);
    }

    @Transactional
    public Friendship update(Integer id, Friendship friendship) throws DataAccessException {
        Friendship friendshipToUpdate = findById(id);
        friendshipToUpdate.setFriendState(friendship.getFriendState());
        return friendshipRepository.save(friendshipToUpdate);
    }

    @Transactional
    public void delete(Integer id) throws DataAccessException {
        friendshipRepository.deleteById(id);
    }
}