export async function getUserByUsername(username, jwt) {
  const response = await fetch(`/api/v1/users/username/${username}`, {
    headers: {
      Authorization: `Bearer ${jwt}`,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) throw new Error(`Player with username ${username} does not exist.`);
  return response.json();
}

export async function getMyFriendships(jwt) {
  const response = await fetch(`/api/v1/friendships`, {
    headers: {
      Authorization: `Bearer ${jwt}`,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) throw new Error('Failed to fetch friendships.');
  return response.json();
}

export async function sendFriendshipRequest(receiver, jwt) {
  const response = await fetch(`/api/v1/friendships`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${jwt}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ receiver: receiver })
  });
  if (!response.ok) throw new Error('Failed to send friendship request.');
  return response;
}

export async function acceptFriendshipApi(friendshipId, jwt) {
  const response = await fetch(`/api/v1/friendships/${friendshipId}/acceptFriendship`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwt}`,
    }
  });
  if (!response.ok) throw new Error('Failed to accept the friendship.');
  return response;
}

export async function rejectFriendshipApi(friendshipId, jwt) {
  const response = await fetch(`/api/v1/friendships/${friendshipId}/rejectFriendship`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwt}`,
    }
  });
  if (!response.ok) throw new Error('Failed to reject the friendship.');
  return response;
}
