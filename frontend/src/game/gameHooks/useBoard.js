import { useState, useEffect, useRef } from 'react'
import { getBoard, getStartingCards } from '../gameUtils/utils'

export function useBoard (game) {
    const [board, setBoard] = useState([])
    const initialized = useRef(false)

    // Initialize board with starting cards on first load
    useEffect(() => {
        if (game != null && !initialized.current) {
            setBoard(getBoard(game))
            initialized.current = true
        }
    }, [game])

    // Sync board with server state (merge server cards with starting cards)
    useEffect(() => {
        if (game?.cards == null || !initialized.current) {
            return
        }

        const startingCards = getStartingCards(game)
        const newBoard = Array(49).fill(null)

        for (const { index, card } of startingCards) {
            newBoard[index] = {
                index,
                rotation: 0,
                card
            }
        }

        for (const card of game.cards) {
            if (card != null) {
                newBoard[card.index] = card
            }
        }

        setBoard(newBoard)
    }, [game?.cards])

    return {
        board,
        setBoard
    }
}
