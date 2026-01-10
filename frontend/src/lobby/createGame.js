import { useNavigate } from 'react-router-dom';
import LinkClickButton from '../components/LinkClickButton.js';
import { useFetchResource } from '../util/useFetchResource.js';
import '../static/css/page.css'
import '../static/css/home/home.css'

export default function CreateGame(){
    const navigate = useNavigate()
    const { getData } = useFetchResource()

    const handleCreateGame = async () => {
        const { data, status } = await getData(
            `/api/v1/games/create`,
            'POST'
        )

        if (status === 'success') {
            navigate(`/lobby/${data.id}`)
        }
    }

    return(
        <div
            className='page-container'
        >
            <div
                className='home-container'
            >
                <img
                    src='/images/logo_transparent.png'
                    alt='Logo'
                />
                <div
                    className='buttons-container'
                >
                    <LinkClickButton
                        onClick={handleCreateGame}
                        text='CREATE GAME'
                    />
                    <LinkClickButton
                        link='/joingame'
                        text='JOIN GAME'
                        className='orange'
                    />
                </div>
            </div>
        </div>
    );
}