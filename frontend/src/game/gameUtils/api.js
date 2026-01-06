import tokenService from '../../services/token.service'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()

export async function placeCard (game, selected, index) {
    const response = await fetch(
        `/api/v1/board/${game.gameId}/place`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`
            },
            body: JSON.stringify({
                cardId: selected.id,
                index: index
            })
        }
    )

    if (!response.ok) {
        throw new Error(`Error placing card: ${response.status}`)
    }
}

export async function giveUp (game, navigate) {
    try {
        const res = await fetch(
            `/api/v1/games/${game.gameId}/giveup`,
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

export async function setUpSkill (game, skill) {
    try {
        const res = await fetch(
            `/api/v1/games/${game.gameId}/setUpSkill`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`,
                },
                body: JSON.stringify({
                    skill: skill
                })
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

export async function changeDeck (game) {
    try {
        await fetch(
            `/api/v1/board/${game.gameId}/change`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`
                }
            }
        )
    } catch (err) {
        console.error('Error fetching deck:', err)
    }
}
