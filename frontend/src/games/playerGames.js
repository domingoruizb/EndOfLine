import { useState } from 'react';
import useFetchState from "../util/useFetchState";
import tokenService from '../services/token.service';
import "../static/css/games/playerGames.css";

function formatDateTime(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function getOpponentUsername(gamePlayers, myUsername) {
  if (!Array.isArray(gamePlayers)) return 'N/A';
  const opponent = gamePlayers
    .map(gp => gp?.user)
    .find(u => u && u.username !== myUsername);
  return opponent?.username || 'N/A';
}

function displayWinnerName(winnerUsername, myUsername) {
  if (!winnerUsername) return 'N/A';
  return winnerUsername === myUsername ? 'You' : winnerUsername;
}

export default function PlayerGamesList() {
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();
  const [games] = useFetchState([], '/api/v1/games', jwt, null, null);
  
  const [pastPage, setPastPage] = useState(1);
  const itemsPerPage = 5;

  const username = currentUser?.username;

  const pastGamesData = games
    .filter(game => game.endedAt !== null)
    .filter(game => game.gamePlayers?.some(gp => gp.user.username === username))
    .sort((a, b) => new Date(b.endedAt) - new Date(a.endedAt));
  
  const totalPastPages = Math.ceil(pastGamesData.length / itemsPerPage);
  const startPast = (pastPage - 1) * itemsPerPage;
  const endPast = startPast + itemsPerPage;

  const visiblePastGames = pastGamesData
    .slice(startPast, endPast)
    .map(game => (
      <tr key={game.id}>
        <td className="text-center">{formatDateTime(game.startedAt)}</td>
        <td className="text-center">{formatDateTime(game.endedAt)}</td>
        <td className="text-center">{displayWinnerName(game.winner?.username, username)}</td>
        <td className="text-center">{getOpponentUsername(game.gamePlayers, username)}</td>
      </tr>
    ));

  return (
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">My Past Games</h1>
        {pastGamesData.length > 0 ? (
          <>
            <table aria-label="past games" className="player-games-table mt-4 text-white">
              <thead>
                <tr>
                  <th className="text-center">Started at</th>
                  <th className="text-center">Ended at</th>
                  <th className="text-center">Winner</th>
                  <th className="text-center">Against</th>
                </tr>
              </thead>
              <tbody>{visiblePastGames}</tbody>
            </table>
            {totalPastPages > 1 && (
              <div className="player-games-pagination text-center mt-4">
                <button
                  onClick={() => setPastPage(prev => Math.max(prev - 1, 1))}
                  disabled={pastPage === 1}
                  className="player-games-pagination-button"
                >
                  Previous
                </button>
                <span className="player-games-pagination-info">
                  Page {pastPage} of {totalPastPages}
                </span>
                <button
                  onClick={() => setPastPage(prev => Math.min(prev + 1, totalPastPages))}
                  disabled={pastPage === totalPastPages}
                  className="player-games-pagination-button"
                >
                  Next
                </button>
              </div>
            )}
          </>
        ) : (
          <p className="text-center text-white mt-4">No past games found.</p>
        )}
      </div>
    </div>
  );
}
