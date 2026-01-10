import GameCodeInput from './lobbyComponents/GameCodeInput.js';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useFetchResource } from '../util/useFetchResource.js';
import { showErrorToast } from '../util/toasts.js';
import '../static/css/newGame/newGame.css';
import '../static/css/page.css'

export default function JoinGame() {
    const navigate = useNavigate();

    const [code, setCode] = useState('');
    const { getData } = useFetchResource()

    const handleJoinGame = async (e) => {
        e.preventDefault();
        const cleanCode = code.toUpperCase().trim();
        if (cleanCode.length !== 6) {
            showErrorToast('Please enter a valid 6-character game code')
            return
        }

        const { status, data } = await getData(
            `/api/v1/games/join/${cleanCode}`,
            'POST'
        )

        if (status === 'success') {
            navigate(`/lobby/${data.id}`)
        }
    };

    return(
        <div className="page-container">
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
    );
}