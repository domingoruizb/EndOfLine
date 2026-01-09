import { useRef, useState, useEffect } from 'react'
import { useGameMessages } from '../gameHooks/useGameMessages'
import GameMessage from './GameMessage'
import '../../static/css/game/gameChat.css'

export default function GameChat ({ game }) {
	const [text, setText] = useState('')
	const listRef = useRef(null)
	const { messages, sendMessage } = useGameMessages(game.gameId)

	const clearInput = () => {
		setText('')
	}

	async function handleSendMessage (e) {
		e.preventDefault()

		if (text.trim() === '') {
			return
		}

		await sendMessage(text.trim(), clearInput)
	}

	useEffect(() => {
		if (listRef.current != null) {
			listRef.current.scrollTop = listRef.current.scrollHeight
		}
	}, [messages])

	return (
		<div
			className='game-chat'
		>
			<div
				className='chat-header'
			>
				CHAT
			</div>
			<div
				ref={listRef}
				className='chat-list'
			>
				{
					messages.length === 0 ? (
						<span
                            className='no-messages'
                        >
							No messages yet
						</span>
					) : (
						messages.map((message) => (
                            <GameMessage
                                key={message.id}
                                game={game}
                                message={message}
                            />
                        ))
					)
				}
			</div>
			<form
				onSubmit={handleSendMessage}
			>
				<input
					value={text}
					onChange={e => setText(e.target.value)}
					placeholder='Type a message'
					type='text'
				/>
				<button
					type='submit'
				>
					Send
				</button>
			</form>
		</div>
	)
}
