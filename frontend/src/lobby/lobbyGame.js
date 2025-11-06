import React from 'react';
import '../App.css';
import '../static/css/newGame/newGame.css'; 
import { useState, useEffect } from 'react';
import { Modal } from 'reactstrap';
import tokenService from "../services/token.service.js";
import getErrorModal from "../util/getErrorModal.js";
import { GameGamePlayerFormInputs } from "./forms/colors.js";
import { useNavigate, useParams } from 'react-router-dom';



export default function LobbyGame(){
    const user = tokenService.getUser();
    const navigate = useNavigate();
    const { gameId } = useParams();

    const [showConfirmModal, setShowConfirmModal] = useState(false);

    const [gameData, setGameData] = useState(null);
  const [selectedColor1, setSelectedColor1] = useState("");
  const [selectedColor2, setSelectedColor2] = useState("");
  const jwt = tokenService.getLocalAccessToken();
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(true);
  const [copiedFeedback, setCopiedFeedback] = useState(false);

  const modal = getErrorModal(setVisible, visible, message);

  const handleOpenCancelConfirm = () => {
        setShowConfirmModal(true);
    };

    const handleCloseCancelConfirm = () => {
        setShowConfirmModal(false);
    };

  const handleSelectColor1 = async (color) => {
    if (color === selectedColor2) {
        setMessage("Please choose a different color for each player");
        setVisible(true);
        return;
    }
    try {
        const response = await fetch(`/api/v1/gameplayers/${gameId}/${user.id}/color`, { 
            method: "PUT", 
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify(color) 
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Failed to update color.");
        }

        setSelectedColor1(color);
        setVisible(false); 
        
    } catch (error) {
        console.error('Error updating color:', error);
        setMessage(`Error updating color: ${error.message || "Could not save color."}`);
        setVisible(true);
    }
  };

  const handleSelectFriendColor = async (color) => {
    const friendId = gameData.gamePlayers.find(gp => gp.user.id !== user.id)?.user.id;
    
    if (!friendId) {
        setMessage("The friend has not joined yet.");
        setVisible(true);
        return;
    }
    
    if (color === selectedColor1) {
        setMessage("The friend must choose a different color than yours.");
        setVisible(true);
        return;
    }
    
    try {
        const response = await fetch(`/api/v1/gameplayers/${gameId}/${friendId}/color`, { 
            method: "PUT", 
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify(color) 
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Failed to update friend's color.");
        }

        setSelectedColor2(color);
        setVisible(false);
        
    } catch (error) {
        console.error('Error updating friend color:', error);
        setMessage(`Error updating color: ${error.message || "Could not save friend's color."}`);
        setVisible(true);
    }
};



  const handleCancelGame = async (idGame) => {
      handleCloseCancelConfirm();
        try {
        const response = await fetch(`/api/v1/games/${idGame}`, { 
            method: "DELETE", 
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            }
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Failed to cancel game.");
        }

        navigate('/creategame');
        
    } catch (error) {
        console.error('Error cancelling game:', error);
        setMessage(`Error cancelling game: ${error.message || "Could not cancel game."}`);
        setVisible(true);
    }
    };

    const confirmCancelModal = (
    <Modal 
        isOpen={showConfirmModal} 
        toggle={handleCloseCancelConfirm}
        className="exit-popup" 
    >
        <div className="modal-content-custom" style={{borderColor: '#FE5B02'}}>
            <h4 style={{color: '#FE5B02', marginTop: '10px'}}>Are you sure you want to cancel the game?</h4>
            
            <div className="modal-footer-custom">
                <button 
                    className="normal-button" 
                    onClick={() => handleCancelGame(gameId)}
                    style={{minWidth: '100px', marginRight: '20px'}}
                >
                    Yes
                </button>
                <button 
                    className="caution-button" 
                    onClick={handleCloseCancelConfirm}
                    style={{minWidth: '100px'}}
                >
                    No
                </button>
            </div>
        </div>
    </Modal>
);

const copyGameCode = async () => {
        if (!gameData || !gameData.code) return; // Salir si no hay código

        try {
            await navigator.clipboard.writeText(gameData.code);
            
            setCopiedFeedback(true); 
        
            setTimeout(() => {
                setCopiedFeedback(false);
            }, 2000);

        } catch (err) {
            console.error('Error copying to clipboard:', err);
            setMessage("Failed to copy code. Please copy manually.");
            setVisible(true);
        }
    };

  useEffect(() => {
        if (!gameId) {
            setLoading(false);
            return;
        }
        
        async function fetchGameData() {
            try {
                const response = await fetch(`/api/v1/games/${gameId}`, { 
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Failed to fetch game data.");
                }
                const data = await response.json();
                
                setGameData(data);

                if (data.round > 0) { 
                    navigate("/game/" + gameId); 
                    return; 
                }

                const hostPlayer = data.gamePlayers.find(gp => gp.user.id === user.id);
                const friendPlayer = data.gamePlayers.find(gp => gp.user.id !== user.id);

                if (hostPlayer) setSelectedColor1(hostPlayer.color);
                if (friendPlayer) setSelectedColor2(friendPlayer.color); 
                else setSelectedColor2(null);
                
                if (loading) {
                    setLoading(false);
                }

            } catch (error) {
                console.error('Error fetching game data:', error);
                if (loading) {
                    setMessage("Could not load game lobby.");
                    setVisible(true);
                    setLoading(false);
                }
            }
        };

        fetchGameData();

        const pollingInterval = 3000;
        const intervalId = setInterval(fetchGameData, pollingInterval);

        return () => clearInterval(intervalId);

    }, [jwt, user.id, gameId, loading]);

  const handleStartGame = async () => {
    if (gameData.gamePlayers.length < 2 || selectedColor1 === selectedColor2) {
        setMessage("Ensure both players are present and colors are different.");
        setVisible(true);
        return;
    }

    try {
        const response = await fetch(
            `/api/v1/games/${gameId}/start`,
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
            }
        );

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Failed to start the game.");
        }

        navigate("/game/" + gameId);

    } catch (error) {
        console.error('Error starting game:', error);
        setMessage(`Error starting game: ${error.message || "Could not transition to game view."}`);
        setVisible(true);
    }
};

  if (loading) {
        return <div className="newGame-page-container"> <div className="hero-div-newGame">Loading...</div> </div>;
    }

    if (!gameData) {
        return <div className="newGame-page-container"> <div className="hero-div-newGame">Game not found.</div> </div>;
    }

  const isFriendJoined = gameData.gamePlayers.length > 1;
  const friendName = isFriendJoined ? gameData.gamePlayers.find(gp => gp.user.id !== user.id)?.user.username : 'Your friend';
  const bothColorsSelected = !!selectedColor1 && !!selectedColor2;

  const isHost = gameData.host.id === user.id;
  const friendGamePlayer = gameData.gamePlayers.find(gp => gp.user.id !== gameData.host.id)

  const isCurrentPlayerFriend = isFriendJoined && user.id === friendGamePlayer?.user.id;

  let myColorState = selectedColor1;
  let myHandler = isHost ? handleSelectColor1 : () => {};

  let friendColorState = selectedColor2;
  let friendHandler = isHost ? handleSelectFriendColor : () => {};
  let friendNameDisplayLogic = isFriendJoined ? friendName : 'Your friend';

  const selectorActiveStyle = {pointerEvents: 'auto', opacity: 1}; 
  const selectorInactiveStyle = {
      pointerEvents: 'none', 
      opacity: 0.4, 
      backgroundColor: '#1C1C1C', 
      borderRadius: '10px'
  };

  let selectorHostStyle = isHost ? selectorActiveStyle : selectorInactiveStyle;
  let selectorFriendStyle = isHost ? selectorActiveStyle : selectorInactiveStyle;

  return (
        <div className="home-page-container">
            <div className="hero-div-newGame" style={{ width: '80%' }}>
                <h1>NEW GAME</h1>
                <div style={{marginTop: "2%"}}>
                    <>
                        <div className="row" style={{marginBottom: '10px', fontSize: '1.2em'}}> {isHost && !isFriendJoined ? "Share the following code with your friend:": "" }</div>
                        <div className="row" style={{marginBottom: '10px', fontSize: '1.2em'}}> {!isHost ? "Waiting for " + gameData.host.username + " to start the game" : "" }</div>
                        <div className="row" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '20px 0' }}>
                            {isHost && (
                            <div 
                                style={{
                                    display: 'flex', 
                                    flexDirection: 'column', 
                                    alignItems: 'center',
                                    width: '100%',
                                    maxWidth: '250px'
                                }}
                            >
                            <div 
                                style={{
                                    fontSize: '1.8em',
                                    fontWeight: 'bold',
                                    color: '#FE5B02',
                                    padding: '10px 15px',
                                    border: '2px dashed #B1D12D', 
                                    borderRadius: '5px',
                                    marginBottom: '10px',
                                    cursor: 'pointer'
                                }}
                                onClick={copyGameCode}
                            >
                                {gameData.code}
                            </div> 
                                <button
                                    className="fuente"
                                    onClick={copyGameCode}
                                    style={{
                                        backgroundColor: copiedFeedback ? '#FE5B02' : '#2C2C2C', 
                                        color: copiedFeedback ? '#2C2C2C' : '#FE5B02',
                                        border: `2px solid ${copiedFeedback ? '#FE5B02' : '#FE5B02'}`,
                                        padding: '5px 15px',
                                        borderRadius: '5px',
                                        fontSize: '1em',
                                        minWidth: '200px',
                                        transition: 'all 0.3s ease 0s'
                                    }}
                                >
                                    {copiedFeedback ? 'COPIED!' : 'COPY CODE'}
                                </button>
                        </div>
                        )}
                        </div>


                        <div className="row">
                            <div className="colors"> You </div>
                            <div className="colors"> 
                                <span style={{color: isFriendJoined ? 'white' : '#B1D12D'}}>
                                    {friendNameDisplayLogic}
                                </span>
                            </div>
                        </div>

                        <div className="row">
                            <div className="colors"
                            style={selectorHostStyle}>
                                {GameGamePlayerFormInputs.map((choice) => (
                                    <div
                                        key={choice.color}
                                        className={`color-image-container ${myColorState === choice.color ? "selected-container" : ""}`}
                                    >
                                        <img
                                            src={choice.image}
                                            alt={choice.label}
                                            className="color-image"
                                            onClick={() => myHandler(choice.color)}
                                            style={{ width: "100%", height: "auto" }}
                                        />
                                    </div>
                                ))}
                            </div>
                            
                            <div className="colors" style={selectorFriendStyle}>
                                {GameGamePlayerFormInputs.map((choice) => (
                                    <div
                                        key={choice.color}
                                        className={`color-image-container ${friendColorState === choice.color ? "selected-container" : ""}`}
                                    >
                                        <img
                                            src={choice.image}
                                            alt={choice.label}
                                            className="color-image"
                                            onClick={() => friendHandler(choice.color)}
                                            style={{ width: "100%", height: "auto" }}
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>
                        
                        {modal}
                        {isHost ? (
                          <div className="button-container">
                              <button className="fuente button-style" onClick={handleOpenCancelConfirm}> 
                                  Cancel
                              </button>
                              <button 
                                  className="fuente button-style" 
                                  onClick={handleStartGame}
                                  disabled={!isFriendJoined || !bothColorsSelected || selectedColor1 === selectedColor2} 
                              >
                                  {isFriendJoined && bothColorsSelected ? 'Continue' : 'Waiting...'}
                              </button>
                          </div>
                        ): (
                          <></>
                        )
                        }
                    </>
                </div>
                {modal}
                {confirmCancelModal}
            </div>
        </div>
    );
}