import { useState } from 'react';
import useFetchState from "../util/useFetchState";
import tokenService from '../services/token.service';
import Pagination from '../components/Pagination';
import Table from '../components/Table';

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
  
  const [pastPage, setPastPage] = useState(0);
  const itemsPerPage = 5;

  const username = currentUser?.username;

  const pastGamesData = games
    .filter(game => game.endedAt !== null)
    .filter(game => game.gamePlayers?.some(gp => gp.user.username === username))
    .sort((a, b) => new Date(b.endedAt) - new Date(a.endedAt));
  
  const totalPastPages = Math.ceil(pastGamesData.length / itemsPerPage);
  const startPast = pastPage * itemsPerPage;
  const endPast = startPast + itemsPerPage;

  const rows = pastGamesData
    .slice(startPast, endPast)
    .map(game => [
      formatDateTime(game.startedAt),
      formatDateTime(game.endedAt),
      displayWinnerName(game.winner?.username, username),
      getOpponentUsername(game.gamePlayers, username)
    ]);

  return (
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">My Past Games</h1>
        {pastGamesData.length > 0 ? (
          <>
            <Table
              aria-label='past games'
              columns={['Started at', 'Ended at', 'Winner', 'Against']}
              rows={rows}
            />
            {totalPastPages > 1 && (
              <Pagination
                page={pastPage}
                setPage={setPastPage}
                pages={totalPastPages}
                hasPrevious={pastPage > 0}
                hasNext={pastPage < totalPastPages - 1}
              />
            )}
          </>
        ) : (
          <p className="text-center text-white mt-4">No past games found.</p>
        )}
      </div>
    </div>
  );
}
