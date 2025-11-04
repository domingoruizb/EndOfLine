import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import "../static/css/auth/authButton.css";
import "../static/css/auth/authPage.css";
import logo from '../static/images/EndOfLineProjectLogo.png';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import tokenService from "../services/token.service.js";



export default function CreateGame(){
    const location = useLocation();
    const navigate = useNavigate();
    const [message, setMessage] = useState("");

    const user = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();

    useEffect(() => {
    if (location.state?.message) {
      setMessage(location.state.message);
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  const handleCreateGame = async () => {
        if (!user || !jwt) {
            setMessage("You must be logged in to create a game.");
            return;
        }

        try {
            const response = await fetch(
                `/api/v1/games/create/${user.id}`, 
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
                throw new Error(errorText || "Failed to create game.");
            }

            const newGame = await response.json();
            
            navigate(`/lobby/${newGame.id}`); 

        } catch (error) {
            console.error('Error creating game:', error);
            setMessage(`Error creating game: ${error.message || "Could not start game."}`);
        }
    };

    return(
        <>
        {message && (
            <div className="message">
                {message}
            </div>
        )}
        <div className="home-page-container">
            <div className="hero-div">
                <h1>END OF LINE</h1>
                <img src={logo} alt="Logo" style={{ width: '500px', height: 'auto', borderRadius: '50px' }} />
                {
                    <div className="options-row">
                        <Link className="auth-button" to="/joingame" style={{textDecoration: "none"}}>
                            JOIN GAME
                        </Link>
                        <button 
                            className="auth-button" 
                            onClick={handleCreateGame}
                        >
                            CREATE GAME
                        </button>
                    </div>
                }
            </div>
        </div>
        </>
    );
}