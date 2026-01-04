import { useState, useEffect, useCallback, useRef } from 'react'
import tokenService from '../../services/token.service'

export function useDeck (gameId, round) {
    const [deck, setDeck] = useState(null)
    const deckRef = useRef([])
    const prevRoundRef = useRef(null)
    const initialFetchDone = useRef(false)

    const fetchDeck = useCallback(async (requestNewDeck = false) => {
        if (gameId == null) {
            return
        }

        try {
            const jwt = tokenService.getLocalAccessToken()
            // Send empty array for new deck, or current deck for more cards
            const body = requestNewDeck ? [] : deckRef.current
            console.log(body)

            const response = await fetch(`/api/v1/board/${gameId}/deck`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`
                },
                body: JSON.stringify(body)
            })

            if (!response.ok) {
                throw new Error(`Error fetching deck: ${response.status}`)
            }

            const data = await response.json()
            deckRef.current = data
            setDeck(data)
        } catch (err) {
            console.error('Error fetching deck:', err)
        }
    }, [gameId])

    // Initial fetch with empty array to get whole deck
    useEffect(() => {
        if (initialFetchDone.current) return
        initialFetchDone.current = true
        fetchDeck(true)
    }, [fetchDeck])

    // Refill deck when round increases
    useEffect(() => {
        if (round == null) return

        if (prevRoundRef.current != null && round > prevRoundRef.current) {
            fetchDeck(false)
        }

        prevRoundRef.current = round
    }, [round, fetchDeck])

    // Remove a card from the deck (after placing)
    const removeCard = useCallback((cardId) => {
        const newDeck = deckRef.current.filter(card => card.id !== cardId)
        deckRef.current = newDeck
        setDeck(newDeck)
        return newDeck
    }, [])

    return {
        deck,
        // Remove a card from deck after placing
        removeCard,
        // Request a completely new deck (empty body) - for start or round 1 deck change
        requestNewDeck: () => fetchDeck(true),
        // Request more cards (sends current deck so server can calculate available cards)
        requestMoreCards: () => fetchDeck(false)
    }
}
