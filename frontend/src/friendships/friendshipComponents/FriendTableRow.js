import { FriendActionGroup } from "./FriendActionGroup";

export default function FriendTableRow({
  friendship, user, friendshipType, onAccept, onReject, onSpectate, onDelete, canSpectate, getFriendActiveGame
}) {
  const isSender = friendship.sender.id === user.id;
  const otherUser = isSender ? friendship.receiver : friendship.sender;
  return (
    <tr key={friendship.id}>
      <td className="text-center">{otherUser.username}</td>
      <td className="text-center">
        <FriendActionGroup
          isPending={friendshipType === "PENDING"}
          isSender={isSender}
          onAccept={() => onAccept(friendship.id)}
          onReject={() => onReject(friendship.id)}
          onSpectate={() => onSpectate(getFriendActiveGame(otherUser.id)?.id)}
          onDelete={() => onDelete(friendship.id)}
          canSpectate={canSpectate}
        />
      </td>
    </tr>
  );
}
