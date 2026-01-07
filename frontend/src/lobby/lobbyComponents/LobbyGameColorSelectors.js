import React from 'react';
import ColorSelector from './ColorSelector';
import { colorImages } from '../../util/colors';

export default function LobbyGameColorSelectors({
  myColorState,
  myHandler,
  friendColorState,
  friendHandler,
  isHost
}) {
  return (
    <div className="row lobby-game-row lobby-game-row-selectors">
      <ColorSelector
        colorImages={colorImages}
        selectedColor={myColorState}
        onSelect={myHandler}
        disabled={!isHost}
      />
      <ColorSelector
        colorImages={colorImages}
        selectedColor={friendColorState}
        onSelect={friendHandler}
        disabled={!isHost}
      />
    </div>
  );
}
