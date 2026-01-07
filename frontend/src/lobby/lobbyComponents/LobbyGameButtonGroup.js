import React from 'react';

export default function LobbyGameButtonGroup({ isHost, onCancel, onContinue, continueDisabled }) {
  if (!isHost) return null;
  return (
    <div className="button-container lobby-game-btn-container">
      <button className="fuente button-style lobby-game-cancel-btn" onClick={onCancel}>
        Cancel
      </button>
      <button
        className="fuente button-style lobby-game-continue-btn"
        onClick={onContinue}
        disabled={continueDisabled}
      >
        {continueDisabled ? 'Waiting...' : 'Continue'}
      </button>
    </div>
  );
}
