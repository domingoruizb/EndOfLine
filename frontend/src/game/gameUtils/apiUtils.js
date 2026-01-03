import tokenService from '../../services/token.service'
import { getCoordinates } from './cardUtils'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()

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
        )

        if (!res.ok) {
            const errorText = await res.text()
            try {
                const errorData = JSON.parse(errorText)
                throw new Error(errorData.message || 'Failed to set up skill')
            } catch (e) {
                throw new Error(`Failed to set up skill: ${res.status} ${res.statusText}`)
            }
        }

    } catch (error) {
        console.error("Error setting up skill:", error)
        alert(`Error setting up skill: ${error.message}`)
    }
}

export async function loseAndEndGame (
    gameId
) {
    try {
        const res = await fetch(`/api/v1/games/${gameId}/${user.id}/lose`, {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${jwt}`,
                'Content-Type': 'application/json',
            }
        })

        if (!res.ok) {
            throw new Error('Failed to lose the game')
        }
    } catch (error) {
        console.error("Error losing the game:", error)
    }
}

export async function handleGiveUp (
    gameId,
    toggleGiveUpModal,
    navigate
) {
    toggleGiveUpModal()

    try {
        const res = await fetch(
            `/api/v1/games/${gameId}/${user.id}/giveup`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`,
                },
            }
        )

        if (!res.ok) {
            const errorText = await res.text()
            try {
                const errorData = JSON.parse(errorText)
                throw new Error(errorData.message || 'Failed to give up the game')
            } catch (e) {
                throw new Error(`Failed to give up the game: ${res.status} ${res.statusText}`)
            }
        }

        navigate('/creategame')
    } catch (error) {
        console.error("Error giving up the game:", error)
        alert(`Error giving up: ${error.message}`)
    }
}
