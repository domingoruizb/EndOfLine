import LinkClickButton from "../../components/LinkClickButton";

export function FriendActionGroup({
  isPending, isSender, onAccept, onReject, onSpectate, onDelete, canSpectate
}) {
  return (
    <div className="friend-action-group">
      {isPending && !isSender ? (
        <>
          <LinkClickButton
            text="Accept"
            aria-label="accept-friendship"
            className="sm"
            onClick={onAccept}
          />
          <LinkClickButton
            text="Reject"
            aria-label="reject-friendship"
            className="sm danger"
            onClick={onReject}
          />
        </>
      ) : (
        <>
          {canSpectate && (
            <LinkClickButton
              text="Spectate"
              aria-label="spectate-friendship"
              className="sm"
              onClick={onSpectate}
            />
          )}
          <LinkClickButton
            text="Delete"
            aria-label="delete-friendship"
            className="sm danger"
            onClick={onDelete}
          />
        </>
      )}
    </div>
  );
}
