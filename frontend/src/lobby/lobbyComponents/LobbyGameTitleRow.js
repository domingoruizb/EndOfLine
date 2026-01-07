import React from 'react';

export default function LobbyGameTitleRow({ isHost, isFriendJoined, hostName }) {
  return (
    <>
      <div className="row lobby-game-row lobby-game-row-title">
        {isHost && !isFriendJoined ? 'Share the following code with your friend:' : ''}
      </div>
      <div className="row lobby-game-row lobby-game-row-title">
        {!isHost ? `Waiting for ${hostName} to start the game` : ''}
      </div>
    </>
  );
}
