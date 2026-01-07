
import React, { useState } from 'react';
import '../App.css';
import '../static/css/newGame/newGame.css';
import '../static/css/lobby/lobbyGame.css';
import { useNavigate, useParams } from 'react-router-dom';
import tokenService from '../services/token.service.js';
import ErrorModal from './lobbyComponents/ErrorModal.js';
import ConfirmCancelModal from './lobbyComponents/ConfirmCancelModal.js';
import LobbyGameTitleRow from './lobbyComponents/LobbyGameTitleRow';
import LobbyGameCodeBox from './lobbyComponents/LobbyGameCodeBox';
import LobbyGamePlayersRow from './lobbyComponents/LobbyGamePlayersRow';
import LobbyGameColorSelectors from './lobbyComponents/LobbyGameColorSelectors';
import LobbyGameButtonGroup from './lobbyComponents/LobbyGameButtonGroup';
import useLobbyGameData from '../hooks/useLobbyGameData';
import { useLobbyGameActions } from '../hooks/useLobbyGameActions';


export default function LobbyGame() {
  const user = tokenService.getUser();
  const navigate = useNavigate();
  const { gameId } = useParams();

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [copiedFeedback, setCopiedFeedback] = useState(false);
  const jwt = tokenService.getLocalAccessToken();

  const {
    gameData,
    setGameData,
    selectedColor1,
    setSelectedColor1,
    selectedColor2,
    setSelectedColor2,
    loading,
    setLoading,
  } = useLobbyGameData(gameId, user, setMessage, setVisible);

  const actions = useLobbyGameActions({
    gameId,
    user,
    jwt,
    setSelectedColor1,
    setSelectedColor2,
    setMessage,
    setVisible,
    navigate,
  });

  const handleOpenCancelConfirm = () => setShowConfirmModal(true);
  const handleCloseCancelConfirm = () => setShowConfirmModal(false);

  if (loading) {
    return <div className="newGame-page-container"><div className="hero-div-newGame">Loading...</div></div>;
  }
  if (!gameData) {
    return <div className="newGame-page-container"><div className="hero-div-newGame">Game not found.</div></div>;
  }

  const isFriendJoined = gameData.gamePlayers.length > 1;
  const friendName = isFriendJoined ? gameData.gamePlayers.find(gp => gp.user.id !== user.id)?.user.username : 'Your friend';
  const bothColorsSelected = !!selectedColor1 && !!selectedColor2;
  const isHost = gameData.host.id === user.id;
  const myColorState = selectedColor1;
  const myHandler = isHost ? (color) => actions.handleSelectColor1(color, selectedColor2) : () => {};
  const friendColorState = selectedColor2;
  const friendHandler = isHost ? (color) => actions.handleSelectFriendColor(color, gameData, selectedColor1) : () => {};
  const friendNameDisplayLogic = isFriendJoined ? friendName : 'Your friend';

  return (
    <div className="home-page-container">
      <div className="hero-div-newGame lobby-game-container">
        <h1>NEW GAME</h1>
        <div className="lobby-game-content">
          <>
            <LobbyGameTitleRow isHost={isHost} isFriendJoined={isFriendJoined} hostName={gameData.host.username} />
            <div className="row lobby-game-row lobby-game-row-code-container">
              {isHost && (
                <LobbyGameCodeBox
                  code={gameData.code}
                  copiedFeedback={copiedFeedback}
                  onCopy={() => actions.copyGameCode(gameData, setCopiedFeedback, setMessage, setVisible)}
                />
              )}
            </div>
            <LobbyGamePlayersRow isFriendJoined={isFriendJoined} friendNameDisplayLogic={friendNameDisplayLogic} />
            <LobbyGameColorSelectors
              myColorState={myColorState}
              myHandler={myHandler}
              friendColorState={friendColorState}
              friendHandler={friendHandler}
              isHost={isHost}
            />
            <ErrorModal isOpen={visible} message={message} onClose={() => setVisible(false)} />
            <LobbyGameButtonGroup
              isHost={isHost}
              onCancel={handleOpenCancelConfirm}
              onContinue={() => actions.handleStartGame(gameData, selectedColor1, selectedColor2, setMessage, setVisible, navigate)}
              continueDisabled={!isFriendJoined || !bothColorsSelected || selectedColor1 === selectedColor2}
            />
          </>
        </div>
        <ErrorModal isOpen={visible} message={message} onClose={() => setVisible(false)} />
        <ConfirmCancelModal
          isOpen={showConfirmModal}
          onConfirm={() => actions.handleCancelGame(gameId, handleCloseCancelConfirm)}
          onCancel={handleCloseCancelConfirm}
        />
      </div>
    </div>
  );
}