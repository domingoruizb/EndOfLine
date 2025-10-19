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
  .map((d) => (
    <tr key={d.id}>
      <td className="text-center">{d.round > 0 ? d.round : 'In Lobby'}</td>
      <td className="text-center">{d.round > 0 ? new Date(d.startedAt).toLocaleString() : 'N/A'}</td>
      <td className="text-center">{d.host.username}</td>
    </tr>
  ))
  
  const pastGamesList = games.filter(game => game.endedAt !== null && game.round > 0)
  .map((d) => (
    <tr key={d.id}>
      <td className="text-center">{new Date(d.startedAt).toLocaleString()}</td>
      <td className="text-center">{new Date(d.endedAt).toLocaleString()}</td>
      <td className="text-center">{d.host.username}</td>
    </tr>
  ))
  
  return (
    <div>
      <div className="admin-page-container mt-4">
        <h1 className="text-center">Ongoing Games</h1>
        <div>
          <Table
            aria-label="ongoing games"
            className="mt-4"
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
          </Table>
        </div>
      </div>
      <div className="admin-page-container mt-4">
        <h1 className="text-center">Past Games</h1>
        <div>
          <Table aria-label="past games" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Started at</th>
                <th className="text-center">Ended at</th>
                <th className="text-center">Creator</th>
                <th className="text-center">Players</th>
              </tr>
            </thead>
            <tbody>{pastGamesList}</tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}
