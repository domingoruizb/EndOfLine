import React from 'react';
import GameCodeInput from './lobbyComponents/GameCodeInput.js';
import '../App.css';
import '../static/css/newGame/newGame.css'; 
import { useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import tokenService from "../services/token.service.js";
import getErrorModal from "../util/getErrorModal.js";



export default function JoinGame(){
    const location = useLocation();
    const navigate = useNavigate();

    const [code, setCode] = useState('');
    const [message, setMessage] = useState("");
    const [visible, setVisible] = useState(false);

    const user = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();

    const modal = getErrorModal(setVisible, visible, message);

    useEffect(() => {
    if (location.state?.message) {
      setMessage(location.state.message);
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  const handleJoinGame = async (e) => {
        e.preventDefault();
        
        if (!user || !jwt) {
            setMessage("You must be logged in to join a game.");
            setVisible(true);
            return;
        }
        
        const cleanCode = code.toUpperCase().trim();
        if (cleanCode.length !== 6) {
            setMessage('Please enter a valid 6-character game code.');
            setVisible(true);
            return;
        }

        try {

            const response = await fetch(
                `/api/v1/games/join/${user.id}/${cleanCode}`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        "Content-Type": "application/json",
                    }
                }
            );

            if (!response.ok) {
                const errorData = await response.json(); 
                throw new Error(errorData.message || "Failed to join game. Check the code.");
            }

            const gameData = await response.json();
            
            navigate(`/lobby/${gameData.id}`); 

        } catch (error) {
            console.error('Error joining game:', error);
            setMessage(`Error: ${error.message || "Could not join game."}`);
            setVisible(true);
        }
    };

    return(
        <>
        {modal}
        <div className="home-page-container">
            <div className="hero-div-newGame" style={{ width: '40%' }}>
                <h1>JOIN A GAME</h1>
                
                <form onSubmit={handleJoinGame} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <GameCodeInput code={code} setCode={setCode} />
                    <div className="button-container" style={{ flexDirection: 'column', width: '100%'}}>
                        <button type="submit" className="fuente button-style" style={{ minWidth: '200px' }}>
                            JOIN
                        </button>
                    </div>
                </form>
            </div>
        </div>
        </>
    );
}