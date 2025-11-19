import tokenService from '../../services/token.service'
import { getCoordinates } from './cardUtils'

const jwt = tokenService.getLocalAccessToken()

export async function postCardPlacement (
    index,
    rotation,
    selectedCard,
    gamePlayerId,
    isTurnFinished
) {
    const coords = getCoordinates(index)
    try {
        await fetch(
            `/api/v1/cards/place/${gamePlayerId}`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`,
                },
                body: JSON.stringify({
                    image: `/cardImages/${selectedCard}.png`,
                    position_x: coords.row,
                    position_y: coords.col,
                    rotation: rotation,
                    turnFinished: isTurnFinished
                })
            }
        )
    } catch (err) {
        console.error('Error in postCardPlacement:', err)
    }
}

export async function setUpSkill (skill, gameId, userId) {
        try {
            const res = await fetch(
                `/api/v1/games/${gameId}/${userId}/setUpSkill`,
                {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${jwt}`,
                    },
                    body: JSON.stringify({ skill: skill })
                }
            );

            if (!res.ok) {
                const errorText = await res.text();
                try {
                    const errorData = JSON.parse(errorText);
                    throw new Error(errorData.message || 'Failed to set up skill');
                } catch (e) {
                    throw new Error(`Failed to set up skill: ${res.status} ${res.statusText}`);
                }
            }

        } catch (error) {
            console.error("Error setting up skill:", error);
            alert(`Error setting up skill: ${error.message}`);
        }

    };
