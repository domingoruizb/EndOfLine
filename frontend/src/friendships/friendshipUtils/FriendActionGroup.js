import { Button } from "reactstrap";

export function FriendActionGroup({
  isPending, isSender, onAccept, onReject, onSpectate, onDelete, canSpectate
}) {
  return (
    <div className="friend-action-group">
      {isPending && !isSender ? (
        <>
          <Button
            aria-label="accept-friendship"
            size="sm"
            className="positive-button"
            onClick={onAccept}
          >
            Accept
          </Button>
          <Button
            aria-label="reject-friendship"
            size="sm"
            className="negative-button"
            onClick={onReject}
          >
            Reject
          </Button>
        </>
      ) : (
        <>
          {canSpectate && (
            <Button
              aria-label="spectate-friendship"
              size="sm"
              className="positive-button"
              onClick={onSpectate}
            >
              Spectate
            </Button>
          )}
          <Button
            aria-label="delete-friendship"
            size="sm"
            className="negative-button"
            onClick={onDelete}
          >
            Delete
          </Button>
        </>
      )}
    </div>
  );
}
