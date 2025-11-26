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

  return (
    <div style={{ width: '280px', display: 'flex', flexDirection: 'column', gap: '6px' }}>
      <div style={{ fontWeight: '600' }}>Chat</div>
      <div ref={listRef} style={{ height: '220px', overflowY: 'auto', background: '#fff', padding: '8px', borderRadius: '6px', border: '1px solid #ddd' }}>
        {messages.length === 0 && (
          <div style={{ color: '#888', fontSize: '13px' }}>No messages yet</div>
        )}
        {messages.map((m, idx) => (
          <div key={idx} style={{ marginBottom: '6px' }}>
            <div style={{ fontSize: '12px', color: '#333' }}><strong>{m.sender}</strong></div>
            <div style={{ fontSize: '14px', color: '#222' }}>{m.text}</div>
          </div>
        ))}
      </div>
      <form onSubmit={sendMessage} style={{ display: 'flex', gap: '6px' }}>
        <input value={text} onChange={e => setText(e.target.value)} placeholder='Type a message' style={{ flex: 1, padding: '6px', borderRadius: '4px', border: '1px solid #ccc' }} />
        <button type='submit' style={{ padding: '6px 8px', borderRadius: '4px' }}>Send</button>
      </form>
    </div>
  )
}
