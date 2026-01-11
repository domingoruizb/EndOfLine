export function useLobbyGameActions({ gameId, user, jwt, setMessage, setVisible, setSelectedColor1, setSelectedColor2, navigate }) {
  const handleSelectColor1 = async (color, selectedColor2) => {
    if (color === selectedColor2) {
      setMessage('Please choose a different color for each player');
      setVisible(true);
      return;
    }
    try {
      const response = await fetch(`/api/v1/gameplayers/${gameId}/${user.id}/color`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(color),
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to update color.');
      }
      setSelectedColor1(color);
      setVisible(false);
    } catch (error) {
      setMessage(`Error updating color: ${error.message || 'Could not save color.'}`);
      setVisible(true);
    }
  };

  // Select color for friend
  const handleSelectFriendColor = async (color, gameData, selectedColor1) => {
    const friendId = gameData.gamePlayers.find(gp => gp.user.id !== user.id)?.user.id;
    if (!friendId) {
      setMessage('The friend has not joined yet.');
      setVisible(true);
      return;
    }
    if (color === selectedColor1) {
      setMessage('The friend must choose a different color than yours.');
      setVisible(true);
      return;
    }
    try {
      const response = await fetch(`/api/v1/gameplayers/${gameId}/${friendId}/color`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(color),
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Failed to update friend's color.");
      }
      setSelectedColor2(color);
      setVisible(false);
    } catch (error) {
      setMessage(`Error updating color: ${error.message || "Could not save friend's color."}`);
      setVisible(true);
    }
  };

  // Cancel game
  const handleCancelGame = async (idGame, handleCloseCancelConfirm) => {
    handleCloseCancelConfirm();
    try {
      const response = await fetch(`/api/v1/games/${idGame}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to cancel game.');
      }
      navigate('/creategame');
    } catch (error) {
      setMessage(`Error cancelling game: ${error.message || 'Could not cancel game.'}`);
      setVisible(true);
    }
  };

  // Start game
  const handleStartGame = async (gameData, selectedColor1, selectedColor2, setMessage, setVisible, navigate) => {
    if (gameData.gamePlayers.length < 2 || selectedColor1 === selectedColor2) {
      setMessage('Ensure both players are present and colors are different.');
      setVisible(true);
      return;
    }
    try {
      const response = await fetch(`/api/v1/games/${gameId}/start`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to start the game.');
      }
      navigate('/game/' + gameId);
    } catch (error) {
      setMessage(`Error starting game: ${error.message || 'Could not transition to game view.'}`);
      setVisible(true);
    }
  };

  // Copy game code
  const copyGameCode = async (gameData, setCopiedFeedback, setMessage, setVisible) => {
    if (!gameData || !gameData.code) return;
    try {
      await navigator.clipboard.writeText(gameData.code);
      setCopiedFeedback(true);
      setTimeout(() => {
        setCopiedFeedback(false);
      }, 2000);
    } catch (err) {
      setMessage('Failed to copy code. Please copy manually.');
      setVisible(true);
    }
  };

  return {
    handleSelectColor1,
    handleSelectFriendColor,
    handleCancelGame,
    handleStartGame,
    copyGameCode,
  };
}
