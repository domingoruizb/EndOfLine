import { useState, useEffect, useCallback } from 'react';
import tokenService from '../../services/token.service.js';
import { useNavigate } from 'react-router-dom';

export default function useLobbyGameData(gameId, user, setMessage, setVisible) {
  const [gameData, setGameData] = useState(null);
  const [selectedColor1, setSelectedColor1] = useState("");
  const [selectedColor2, setSelectedColor2] = useState("");
  const [loading, setLoading] = useState(true);
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();

  const fetchGameData = useCallback(async () => {
    try {
      const response = await fetch(`/api/v1/games/${gameId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      if (!response.ok) {
        throw new Error('Failed to fetch game data.');
      }
      const data = await response.json();
      setGameData(data);
      if (data.round > 0) {
        navigate('/game/' + gameId);
        return;
      }
      const hostPlayer = data.gamePlayers.find(gp => gp.user.id === user.id);
      const friendPlayer = data.gamePlayers.find(gp => gp.user.id !== user.id);
      if (hostPlayer) setSelectedColor1(hostPlayer.color);
      if (friendPlayer) setSelectedColor2(friendPlayer.color);
      else setSelectedColor2(null);
      setLoading(false);
    } catch (error) {
      setMessage('Could not load game lobby.');
      setVisible(true);
      setLoading(false);
    }
  }, [gameId, jwt, user.id, navigate, setMessage, setVisible]);

  useEffect(() => {
    if (!gameId) {
      setLoading(false);
      return;
    }
    fetchGameData();
    const intervalId = setInterval(fetchGameData, 3000);
    return () => clearInterval(intervalId);
  }, [gameId, fetchGameData]);

  return {
    gameData,
    setGameData,
    selectedColor1,
    setSelectedColor1,
    selectedColor2,
    setSelectedColor2,
    loading,
    setLoading,
  };
}
