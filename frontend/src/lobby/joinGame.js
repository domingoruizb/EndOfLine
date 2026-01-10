import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useFetchResource } from '../util/useFetchResource.js';
import { showErrorToast } from '../util/toasts.js';
import LinkClickButton from '../components/LinkClickButton.js';
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
            <div className="info-container">
                <h1 className='info-title'>JOIN A GAME</h1>
                <div
                    className='form-container'
                >
                    <form onSubmit={handleJoinGame} className='join-form'>
                        <label htmlFor="gameCode" className='join-label'>
                            Enter Game Code:
                        </label>
                        <input
                            type="text"
                            id="gameCode"
                            name="gameCode"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            maxLength={6}
                            required
                            placeholder="A1B2C3"
                            className='join-input'
                        />
                        <LinkClickButton
                            text="JOIN"
                            onClick={handleJoinGame}
                            className='orange'
                        />
                    </form>
                </div>
            </div>
        </div>
    );
}