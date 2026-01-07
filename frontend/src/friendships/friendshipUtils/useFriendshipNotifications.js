import { useEffect, useRef } from "react";
import { showSuccessToast, showErrorToast } from "./toastUtils";

export default function useFriendshipNotifications(friendships, user, setFriendships, jwt) {
  const notifiedPendingRef = useRef(new Set());

  useEffect(() => {
    if (!friendships) return;
    try {
      const newPendingRequests = friendships.filter(
        (f) => f.friendState === "PENDING" && f.receiver.id === user.id
      );
      newPendingRequests.forEach((request) => {
        if (!notifiedPendingRef.current.has(request.id)) {
          showSuccessToast(`${request.sender.username} sent you a friendship request!`);
          notifiedPendingRef.current.add(request.id);
        }
      });
    } catch (error) {
      showErrorToast("Error processing friendship notifications");
    }
  }, [friendships, user.id]);
}
