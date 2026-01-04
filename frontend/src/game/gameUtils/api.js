import tokenService from '../../services/token.service'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()

export async function placeCard (gameId, selected, index) {
    const response = await fetch(`/api/v1/board/${gameId}/place`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${jwt}`
        },
        body: JSON.stringify({
            cardId: selected.id,
            index: index
        })
    })

    if (!response.ok) {
        throw new Error(`Error placing card: ${response.status}`)
    }
}

export async function giveUp (
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
