import tokenService from '../../services/token.service'
import { getCoordinates } from './cardUtils'

const jwt = tokenService.getLocalAccessToken()

export async function postCardPlacement (
    index,
    rotation,
    selectedCard,
    gamePlayerId
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
                })
            }
        )
    } catch (err) {
        console.error('Error in postCardPlacement:', err)
    }
}
