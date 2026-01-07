import React from 'react';
import '../../static/css/social/RankingTable.css';

const medals = [
  '🥇', // 1st
  '🥈', // 2nd
  '🥉', // 3rd
];

export default function RankingTable({ rankings, currentRankings }) {
  const data = currentRankings || rankings;
  if (!data || data.length === 0) {
    return <div className="ranking-table-empty">No rankings available.</div>;
  }
  const sorted = [...data].sort((a, b) => (b.gamesWon ?? 0) - (a.gamesWon ?? 0));

  return (
    <div className="ranking-table-wrapper">
      <table className="ranking-table">
        <thead>
          <tr>
            <th>Pos</th>
            <th>Player</th>
            <th>Victories</th>
          </tr>
        </thead>
        <tbody>
          {sorted.map((player, idx) => (
            <tr key={player.userId || player.id || player.username || idx}>
              <td className="ranking-medal">
                {idx < 3 ? medals[idx] : idx + 1}
              </td>
              <td>{player.username || player.name}</td>
              <td>{player.gamesWon ?? 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}