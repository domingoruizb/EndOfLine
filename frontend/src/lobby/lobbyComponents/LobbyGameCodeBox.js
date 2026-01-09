import React from 'react';

export default function LobbyGameCodeBox({ code, copiedFeedback, onCopy }) {
  return (
    <div className="lobby-game-code-box-container">
      <div className="lobby-game-code-box" onClick={onCopy}>
        {code}
      </div>
      <button
        className={`fuente lobby-game-copy-btn${copiedFeedback ? ' copied' : ''}`}
        onClick={onCopy}
      >
        {copiedFeedback ? 'COPIED!' : 'COPY CODE'}
      </button>
    </div>
  );
}
