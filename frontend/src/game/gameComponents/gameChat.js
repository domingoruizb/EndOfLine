import React, { useEffect, useRef, useState } from 'react'

export default function GameChat ({ gameId, jwt, user }) {
  const [messages, setMessages] = useState([])
  const [text, setText] = useState('')
  const [lastTs, setLastTs] = useState(0)
  const mounted = useRef(true)
  const listRef = useRef(null)
  const fetchInProgressRef = useRef(false)

  useEffect(() => {
    mounted.current = true
    let cancelled = false

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
      if (!cancelled) setTimeout(poll, 3000) // poll every 3s
    }

    // start polling loop
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
        // deduplicate: only append messages that are not already present
        setMessages(prev => {
          const newFiltered = data.filter(d => !prev.some(p => p.timestamp === d.timestamp && p.sender === d.sender && p.text === d.text))
          if (newFiltered.length === 0) return prev
          return [...prev, ...newFiltered]
        })
        const maxTs = Math.max(...data.map(d => d.timestamp))
        setLastTs(maxTs)
        // scroll to bottom
        setTimeout(() => {
          if (listRef.current) listRef.current.scrollTop = listRef.current.scrollHeight
        }, 50)
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
      if (!res.ok) {
        console.error('failed to send chat', res.status)
        return
      }
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
          return (
            <div key={idx} className={`chat-message ${isMe ? 'me' : 'other'}`}>
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
