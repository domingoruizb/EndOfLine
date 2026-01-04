import { useState, useEffect, useCallback } from 'react'
import tokenService from '../../services/token.service'

const pollingInterval = 1000

export function useGame (gameId) {
    const [game, setGame] = useState(null)

    const fetchGame = useCallback(async () => {
        if (gameId == null) {
            return
        }

        try {
            const jwt = tokenService.getLocalAccessToken()
            const response = await fetch(`/api/v1/board/${gameId}/state`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${jwt}`
                }
            })

            if (!response.ok) {
                throw new Error(`Error fetching game state: ${response.status}`)
            }

            const data = await response.json()
            setGame(data)
        } catch (error) {
            console.error('Error fetching game state:', error)
        }
    }, [gameId])

    useEffect(() => {
        fetchGame()

        const intervalId = setInterval(fetchGame, pollingInterval)

        return () => clearInterval(intervalId)
    }, [fetchGame])

    return {
        game
    }
}
