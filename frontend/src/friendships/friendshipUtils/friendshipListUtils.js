export function filterAndSortFriendships(friendships, friendshipType, userId) {
  if (!friendships) return [];
  const filtered = friendships.filter(f => f.friendState === friendshipType);
  return [...filtered].sort((a, b) => {
    const isAReceiver = a.receiver.id === userId;
    const isBReceiver = b.receiver.id === userId;
    if (isAReceiver && !isBReceiver && a.friendState === "PENDING") return -1;
    else if (!isAReceiver && isBReceiver && b.friendState === "PENDING") return 1;
    else return 0;
  });
}

export function paginate(items, currentPage, itemsPerPage) {
  const indexOfLast = currentPage * itemsPerPage;
  const indexOfFirst = indexOfLast - itemsPerPage;
  return items.slice(indexOfFirst, indexOfLast);
}
