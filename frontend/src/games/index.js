import {
  Table
} from "reactstrap";
import useFetchState from "../util/useFetchState";
import tokenService from '../services/token.service';

const jwt = tokenService.getLocalAccessToken();

export default function GamesList () {
  const [games] = useFetchState(
    [],
    '/api/v1/games',
    jwt,
    null,
    null
  );
  
  const ongoingGamesList = games.filter(game => game.endedAt === null && game.round >= 0)
  .sort((a, b) => a.round - b.round)
  .map((d) => (
    <tr key={d.id}>
      <td className="text-center">{d.round > 0 ? d.round : 'In Lobby'}</td>
      <td className="text-center">{d.round > 0 ? new Date(d.startedAt).toLocaleString() : 'N/A'}</td>
      <td className="text-center">{d.host.username}</td>
      <td className="text-center">{d.gamePlayers.map((gp) => gp.user.username).join(' VS ')}</td>
    </tr>
  ))
  
  const pastGamesList = games.filter(game => game.endedAt !== null && game.round > 0)
  .sort((a, b) => new Date(b.endedAt) - new Date(a.endedAt))
  .map((d) => (
    <tr key={d.id}>
      <td className="text-center">{new Date(d.startedAt).toLocaleString()}</td>
      <td className="text-center">{new Date(d.endedAt).toLocaleString()}</td>
      <td className="text-center">{d.host.username}</td>
			<td className="text-center">{d.gamePlayers.map((gp) => gp.user.username).join(' VS ')}</td>
    </tr>
  ))

  console.log(games)
  
  return (
    <div
      style={{
        backgroundColor: '#000',
        minHeight: '100vh',
        paddingTop: '2rem',
      }}
    >
      <div
        className="admin-page-container"
        style={{
          marginTop: '2rem',
          paddingBottom: '2rem',
          borderBottom: '2px solid #FE5B02',
        }}
      >
        <h1
          className="text-center"
          style={{
            fontSize: '5em',
            fontWeight: 'bold',
            color: "#FE5B02",
          }}
        >
          Ongoing Games
        </h1>
        <div>
          <table
            aria-label="ongoing games"
            className="mt-4 text-white"
            style={{
              borderCollapse: 'separate',
              borderSpacing: '2em 1em',
            }}
          >
            <thead>
              <tr>
                <th className="text-center">Round</th>
                <th className="text-center">Started at</th>
                <th className="text-center">Creator</th>
                <th className="text-center">Players</th>
              </tr>
            </thead>
            <tbody>{ongoingGamesList}</tbody>
          </table>
        </div>
      </div>
      <div className="admin-page-container mt-4">
        <h1
          className="text-center"
          style={{
            fontSize: '5em',
            fontWeight: 'bold',
            color: "#FE5B02",
          }}
        >
          Past Games
        </h1>
        <div>
          <table
            aria-label="past games"
            className="mt-4 text-white"
            style={{
              borderCollapse: 'separate',
              borderSpacing: '2em 1em',
            }}
          >
            <thead>
              <tr>
                <th className="text-center">Started at</th>
                <th className="text-center">Ended at</th>
                <th className="text-center">Creator</th>
                <th className="text-center">Players</th>
              </tr>
            </thead>
            <tbody>{pastGamesList}</tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
