import React, { useEffect, useRef, useState } from 'react'

const COLOR_MAP = {
  RED: '#E31E25',      // Red
  ORANGE: '#F39514',
  YELLOW: '#FFEF47',   // Amber/Yellow
  GREEN: '#50B15F',    // Your main green
  BLUE: '#00A0E3',     // Blue
  MAGENTA: '#E5087F',  // Magenta
  VIOLET: '#C48FBF',   // Deep Purple
  WHITE: '#C5C6C6'     // Light Grey/White
}

export default function GameChat ({ gameId, jwt, user }) {
  const [messages, setMessages] = useState([])
  const [playerColors, setPlayerColors] = useState({}) // state to store username -> color mapping
  const [text, setText] = useState('')
  const [lastTs, setLastTs] = useState(0)
  const mounted = useRef(true)
  const listRef = useRef(null)
  const fetchInProgressRef = useRef(false)

  useEffect(() => {
    mounted.current = true
    let cancelled = false

    // fetch players once on mount to get their colors
    async function fetchPlayers() {
      try {
        const res = await fetch(`/api/v1/games/${gameId}`, {
          headers: { Authorization: `Bearer ${jwt}` }
        })
        if (res.ok) {
          const data = await res.json()
          const colorMap = {}
          if (data.gamePlayers) {
            data.gamePlayers.forEach(p => {
              // map the username (or id) to the chosen color
              const name = p.user.username || p.user.name || p.user.id
              colorMap[name] = p.color // e.g. "RED", "BLUE"
            })
            setPlayerColors(colorMap)
          }
        }
      } catch (err) {
        console.error("Failed to fetch player colors", err)
      }
    }
    
    fetchPlayers()

    async function poll() {
      if (cancelled) return
      if (!fetchInProgressRef.current) {
        fetchInProgressRef.current = true
        try {
          await fetchMessages()
        } finally {
          fetchInProgressRef.current = false
        }
      }
      if (!cancelled) setTimeout(poll, 3000) 
    }

    poll()

    return () => {
      cancelled = true
      mounted.current = false
    }
  }, [gameId, jwt])

  async function fetchMessages () {
    try {
      const res = await fetch(`/api/v1/games/${gameId}/chat/messages?since=${lastTs}`, {
        headers: {
          Authorization: `Bearer ${jwt}`
        }
      })
      if (!res.ok) return
      const data = await res.json()
      if (!mounted.current) return
      if (data.length > 0) {
        const wasNearBottom = listRef.current
          ? (listRef.current.scrollHeight - listRef.current.scrollTop - listRef.current.clientHeight) < 50
          : true

        setMessages(prev => {
          const newFiltered = data.filter(d => !prev.some(p => p.timestamp === d.timestamp && p.sender === d.sender && p.text === d.text))
          if (newFiltered.length === 0) return prev
          return [...prev, ...newFiltered]
        })
        const maxTs = Math.max(...data.map(d => d.timestamp))
        setLastTs(maxTs)

        if (wasNearBottom) {
          setTimeout(() => {
            if (listRef.current) listRef.current.scrollTop = listRef.current.scrollHeight
          }, 50)
        }
      }
    } catch (err) {
      // ignore transient errors
    }
  }

  async function sendMessage (e) {
    e.preventDefault()
    const t = text.trim()
    if (!t) return
    setText('')
    try {
      const res = await fetch(`/api/v1/games/${gameId}/chat`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${jwt}`
        },
        body: JSON.stringify({ text: t })
      })
      if (!res.ok) return
      const saved = await res.json()
      setMessages(prev => [...prev, saved])
      setLastTs(saved.timestamp)
      setTimeout(() => {
        if (listRef.current) listRef.current.scrollTop = listRef.current.scrollHeight
      }, 50)
    } catch (err) {
      console.error(err)
    }
  }

  const senderName = user?.username || user?.name || user?.id

  return (
    <div className='game-chat' style={{ width: '280px' }}>
      <div className='chat-header'>Chat</div>
      <div ref={listRef} className='chat-list'>
        {messages.length === 0 && (
          <div style={{ color: '#888', fontSize: '13px' }}>No messages yet</div>
        )}
        {messages.map((m, idx) => {
          const isMe = senderName && m.sender === senderName
          
          // lookup the color for this sender
          const userColorName = playerColors[m.sender] // e.g. "RED"
          const bubbleColor = COLOR_MAP[userColorName] || (isMe ? '#FE5B02' : '#2ecc71') // fallback to defaults

          return (
            <div 
              key={idx} 
              className={`chat-message ${isMe ? 'me' : 'other'}`}
              // apply the dynamic background color
              style={{ backgroundColor: bubbleColor }}
            >
              {!isMe && <div className='chat-sender'><strong>{m.sender}</strong></div>}
              <div className='chat-text'>{m.text}</div>
            </div>
          )
        })}
      </div>
      <form onSubmit={sendMessage}>
        <input value={text} onChange={e => setText(e.target.value)} placeholder='Type a message' type='text' />
        <button type='submit'>Send</button>
      </form>
    </div>
  )
}
