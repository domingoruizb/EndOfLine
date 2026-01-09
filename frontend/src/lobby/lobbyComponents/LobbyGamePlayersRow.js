import React from 'react';

export default function LobbyGamePlayersRow({ isFriendJoined, friendNameDisplayLogic }) {
  return (
    <div className="row lobby-game-row lobby-game-row-players">
      <div className="colors"> You </div>
      <div className="colors">
        <span className={isFriendJoined ? 'lobby-game-friend-name' : 'lobby-game-friend-name-waiting'}>
          {friendNameDisplayLogic}
        </span>
      </div>
    </div>
  );
}
