import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useFetchState from "../util/useFetchState";
import tokenService from '../services/token.service';
import LinkClickButton from '../components/LinkClickButton';
import "../static/css/games/adminGames.css";

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

export default function AdminGamesList() {
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();
  const [games] = useFetchState([], '/api/v1/games', jwt, null, null);
  
  const [ongoingPage, setOngoingPage] = useState(1);
  const [pastPage, setPastPage] = useState(1);
  const itemsPerPage = 5;

  const ongoingGamesData = games
    .filter(game => game.endedAt === null)
    .sort((a, b) => (a.round || 0) - (b.round || 0));
  
  const totalOngoingPages = Math.ceil(ongoingGamesData.length / itemsPerPage);
  const startOngoing = (ongoingPage - 1) * itemsPerPage;
  const endOngoing = startOngoing + itemsPerPage;
  
  const ongoingGames = ongoingGamesData
    .slice(startOngoing, endOngoing)
    .map(game => (
      <tr key={game.id}>
        <td className="text-center">{game.round ?? 'N/A'}</td>
        <td className="text-center">{formatDateTime(game.startedAt)}</td>
        <td className="text-center">{game.host?.username || 'Unknown'}</td>
        <td className="text-center">{game.gamePlayers?.map(gp => gp.user.username).join(' VS ')}</td>
        <td className="text-center">
          <LinkClickButton 
            link={`/game/${game.id}`}
            text="Spectate"
            className='sm'
          />
        </td>
      </tr>
    ));

  const pastGamesData = games
    .filter(game => game.endedAt !== null)
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
        <td className="text-center">{game.host?.username || 'Unknown'}</td>
        <td className="text-center">{game.winner?.username || 'N/A'}</td>
        <td className="text-center">{game.gamePlayers?.map(gp => gp.user.username).join(' VS ')}</td>
      </tr>
    ));

  return (
    <div className="page-container">
      <div className="info-container">
        <div className="admin-games-section">
          <h1 className="info-title">Ongoing Games</h1>
        {ongoingGamesData.length > 0 ? (
          <>
            <table aria-label="ongoing games" className="admin-games-table mt-4 text-white">
              <thead>
                <tr>
                  <th className="text-center">Round</th>
                  <th className="text-center">Started at</th>
                  <th className="text-center">Creator</th>
                  <th className="text-center">Players</th>
                  <th className="text-center">Actions</th>
                </tr>
              </thead>
              <tbody>{ongoingGames}</tbody>
            </table>
            {totalOngoingPages > 1 && (
              <div className="admin-games-pagination text-center mt-4">
                <button
                  onClick={() => setOngoingPage(prev => Math.max(prev - 1, 1))}
                  disabled={ongoingPage === 1}
                  className="admin-games-pagination-button"
                >
                  Previous
                </button>
                <span className="admin-games-pagination-info">
                  Page {ongoingPage} of {totalOngoingPages}
                </span>
                <button
                  onClick={() => setOngoingPage(prev => Math.min(prev + 1, totalOngoingPages))}
                  disabled={ongoingPage === totalOngoingPages}
                  className="admin-games-pagination-button"
                >
                  Next
                </button>
              </div>
            )}
          </>
        ) : (
          <p className="text-center text-white mt-4">No ongoing games found.</p>
        )}
        </div>
        <div className="admin-games-section">
          <h1 className="info-title">Past Games</h1>
        {pastGamesData.length > 0 ? (
          <>
            <table aria-label="past games" className="admin-games-table mt-4 text-white">
              <thead>
                <tr>
                  <th className="text-center">Started at</th>
                  <th className="text-center">Ended at</th>
                  <th className="text-center">Creator</th>
                  <th className="text-center">Winner</th>
                  <th className="text-center">Players</th>
                </tr>
              </thead>
              <tbody>{visiblePastGames}</tbody>
            </table>
            {totalPastPages > 1 && (
              <div className="admin-games-pagination text-center mt-4">
                <button
                  onClick={() => setPastPage(prev => Math.max(prev - 1, 1))}
                  disabled={pastPage === 1}
                  className="admin-games-pagination-button"
                >
                  Previous
                </button>
                <span className="admin-games-pagination-info">
                  Page {pastPage} of {totalPastPages}
                </span>
                <button
                  onClick={() => setPastPage(prev => Math.min(prev + 1, totalPastPages))}
                  disabled={pastPage === totalPastPages}
                  className="admin-games-pagination-button"
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
    </div>
  );
}
