import useFetchState from "../util/useFetchState";
import tokenService from '../services/token.service';

export default function GamesList() {
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();
  const [games] = useFetchState([], '/api/v1/games', jwt, null, null);

  const roleName = currentUser?.roles?.[0] || '';
  const isAdmin = roleName.toLowerCase().includes("admin");
  const username = currentUser?.username;

  const ongoingGames = games
    .filter(game => game.endedAt === null)
    .sort((a, b) => (a.round || 0) - (b.round || 0))
    .map(game => (
      <tr key={game.id}>
        <td className="text-center">{game.round ?? 'N/A'}</td>
        <td className="text-center">{game.startedAt ? new Date(game.startedAt).toLocaleString() : 'N/A'}</td>
        <td className="text-center">{game.host?.username || 'Unknown'}</td>
        <td className="text-center">{game.gamePlayers?.map(gp => gp.user.username).join(' VS ')}</td>
      </tr>
    ));

  const visiblePastGames = (() => {
    const past = games
      .filter(game => game.endedAt !== null)
      .sort((a, b) => new Date(b.endedAt) - new Date(a.endedAt));
    const filtered = isAdmin ? past : past.filter(game => game.gamePlayers?.some(gp => gp.user.username === username));
    return filtered.map(game => (
      <tr key={game.id}>
        <td className="text-center">{game.startedAt ? new Date(game.startedAt).toLocaleString() : 'N/A'}</td>
        <td className="text-center">{game.endedAt ? new Date(game.endedAt).toLocaleString() : 'N/A'}</td>
        <td className="text-center">{game.host?.username || 'Unknown'}</td>
        <td className="text-center">{game.winner?.username || 'N/A'}</td>
        <td className="text-center">{game.gamePlayers?.map(gp => gp.user.username).join(' VS ')}</td>
      </tr>
    ));
  })();

  return (
    <div style={{ backgroundColor: '#000', minHeight: '100vh', paddingTop: '2rem' }}>
      {isAdmin && (
        <div className="admin-page-container" style={{ marginTop: '2rem', paddingBottom: '2rem', borderBottom: '2px solid #FE5B02' }}>
          <h1 className="text-center" style={{ fontSize: '5em', fontWeight: 'bold', color: "#FE5B02" }}>Ongoing Games</h1>
          {ongoingGames.length > 0 ? (
            <table aria-label="ongoing games" className="mt-4 text-white" style={{ borderCollapse: 'separate', borderSpacing: '2em 1em' }}>
              <thead>
                <tr>
                  <th className="text-center">Round</th>
                  <th className="text-center">Started at</th>
                  <th className="text-center">Creator</th>
                  <th className="text-center">Players</th>
                </tr>
              </thead>
              <tbody>{ongoingGames}</tbody>
            </table>
          ) : (
            <p className="text-center text-white mt-4">No ongoing games found.</p>
          )}
        </div>
      )}
      <div className="admin-page-container mt-4">
        <h1 className="text-center" style={{ fontSize: '5em', fontWeight: 'bold', color: "#FE5B02" }}>Past Games</h1>
        {visiblePastGames.length > 0 ? (
          <table aria-label="past games" className="mt-4 text-white" style={{ borderCollapse: 'separate', borderSpacing: '2em 1em' }}>
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
        ) : (
          <p className="text-center text-white mt-4">No past games found.</p>
        )}
      </div>
    </div>
  );
}
