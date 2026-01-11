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

export default function AdminGamesList() {
  const jwt = tokenService.getLocalAccessToken();
  const [games] = useFetchState([], '/api/v1/games', jwt, null, null);
  
  const [ongoingPage, setOngoingPage] = useState(0);
  const [pastPage, setPastPage] = useState(0);
  const itemsPerPage = 5;

  const ongoingGamesData = games
    .filter(game => game.endedAt === null)
    .sort((a, b) => (a.round || 0) - (b.round || 0));
  
  const totalOngoingPages = Math.ceil(ongoingGamesData.length / itemsPerPage);
  const startOngoing = ongoingPage * itemsPerPage;
  const endOngoing = startOngoing + itemsPerPage;

  const pastGamesData = games
    .filter(game => game.endedAt !== null)
    .sort((a, b) => new Date(b.endedAt) - new Date(a.endedAt));
  
  const totalPastPages = Math.ceil(pastGamesData.length / itemsPerPage);
  const startPast = pastPage * itemsPerPage;
  const endPast = startPast + itemsPerPage;

  const onGoingRows = ongoingGamesData
    .slice(startOngoing, endOngoing)
    .map(game => [
      game.round ?? 'N/A',
      formatDateTime(game.startedAt),
      game.host?.username || 'Unknown',
      game.gamePlayers?.map(gp => gp.user.username).join(' VS '),
    ]);

  const onGoingActions = [
    (rowIndex) => ({
      text: 'Spectate',
      link: `/game/${ongoingGamesData.slice(startOngoing, endOngoing)[rowIndex].id}`,
      className: 'sm'
    })
  ];

  const pastRows = pastGamesData
    .slice(startPast, endPast)
    .map(game => [
      formatDateTime(game.startedAt),
      formatDateTime(game.endedAt),
      game.host?.username || 'Unknown',
      game.winner?.username || 'N/A',
      game.gamePlayers?.map(gp => gp.user.username).join(' VS ')
    ]);

  return (
    <div className="page-container">
      <div className="info-container">
        <div className="info-section">
          <h1 className="info-title">Ongoing Games</h1>
        {ongoingGamesData.length > 0 ? (
          <>
            <Table
              aria-label='ongoing games'
              columns={['Round', 'Started at', 'Creator', 'Players']}
              rows={onGoingRows}
              actions={onGoingActions}
            />
            {totalOngoingPages > 1 && (
              <Pagination
                page={ongoingPage}
                setPage={setOngoingPage}
                pages={totalOngoingPages}
                hasPrevious={ongoingPage > 0}
                hasNext={ongoingPage < totalOngoingPages - 1}
              />
            )}
          </>
        ) : (
          <p className="text-center text-white">No ongoing games found.</p>
        )}
        </div>
        <div className="info-section">
          <h1 className="info-title">Past Games</h1>
        {pastGamesData.length > 0 ? (
          <>
            <Table
              aria-label='past games'
              columns={['Started at', 'Ended at', 'Creator', 'Winner', 'Players']}
              rows={pastRows}
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
    </div>
  );
}
