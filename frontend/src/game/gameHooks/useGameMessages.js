import { useEffect, useState } from 'react'
import tokenService from '../../services/token.service'

const pollingInterval = 3000
const jwt = tokenService.getLocalAccessToken()

export function useGameMessages (gameId) {
    const [messages, setMessages] = useState([])
    const [lastSentAt, setLastSentAt] = useState(null)

    const fetchMessages = async () => {
        try {
            const response = await fetch(`/api/v1/messages/${gameId}${lastSentAt != null ? `?since=${new Date(lastSentAt).toISOString().slice(0, -1)}` : ''}`, {
                headers: {
                    Authorization: `Bearer ${jwt}`
                }
            })

            if (!response.ok) {
                throw new Error(`Error fetching messages: ${response.status}`)
            }

            const data = await response.json()
            if (data.length === 0) {
                return
            }

            setMessages(prev => {
                const newFiltered = data.filter(d => !prev.some(p => p.id === d.id))
                if (newFiltered.length === 0) {
                    return prev
                }
                return [...prev, ...newFiltered]
            })

            const maxSentAt = Math.max(...data.map(d => new Date(d.sentAt).getTime()))
            setLastSentAt(maxSentAt)
        } catch (err) {
            console.error('Error fetching messages:', err)
        }
    }

    const sendMessage = async (text, setText) => {
        try {
            const response = await fetch(`/api/v1/messages/${gameId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`
                },
                body: JSON.stringify({
                    text
                })
            })

            if (!response.ok) {
                throw new Error(`Error sending message: ${response.status}`)
            }

            const saved = await response.json()
            setMessages(prev => [...prev, saved])
            setText('')
        } catch (err) {
            console.error('Error sending message:', err)
        }
    }

    useEffect(() => {
        fetchMessages()

        const intervalId = setInterval(fetchMessages, pollingInterval)

        return () => {
            clearInterval(intervalId)
        }
    }, [gameId])

    return {
        messages,
        sendMessage
    }
}
