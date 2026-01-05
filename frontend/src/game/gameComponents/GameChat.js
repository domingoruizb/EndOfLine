import { useRef, useState, useEffect } from 'react'
import tokenService from '../../services/token.service'
import { useGameMessages } from '../gameHooks/useGameMessages'

const COLOR_MAP = {
	RED: '#E31E25',
	ORANGE: '#F39514',
	YELLOW: '#FFEF47',
	GREEN: '#50B15F',
	BLUE: '#00A0E3',
	MAGENTA: '#E5087F',
	VIOLET: '#C48FBF',
	WHITE: '#C5C6C6'
}

const user = tokenService.getUser()

export default function GameChat ({ game }) {
	const [text, setText] = useState('')
	const listRef = useRef(null)
	const { messages, sendMessage } = useGameMessages(game.gameId)

	async function handleSendMessage (e) {
		e.preventDefault()

		if (text.trim() === '') {
			return
		}

		await sendMessage(text.trim(), setText)
	}

	useEffect(() => {
		if (listRef.current != null) {
			listRef.current.scrollTop = listRef.current.scrollHeight
		}
	}, [messages])

	return (
		<div
			className='game-chat'
			style={{
				width: '280px'
			}}
		>
			<div
				className='chat-header'
			>
				Chat
			</div>
			<div
				ref={listRef}
				className='chat-list'
			>
				{
					messages.length === 0 ? (
						<div style={{
							color: '#888', fontSize: '13px'
						}}>
							No messages yet
						</div>
					) : (
						messages.map((message) => {
							const isOwnMessage = message.sender === user.username

							const player = game.players.find(p => p.username === message.sender)

							const isSpectator = player == null
							const bubbleColor = isSpectator ? (
								COLOR_MAP.WHITE
							) : (
								COLOR_MAP[player.color] ?? (isOwnMessage ? '#FE5B02' : '#2ECC71')
							)

							return (
								<div
									key={message.id}
									className={`chat-message ${isOwnMessage ? 'me' : 'other'}`}
									style={{
										backgroundColor: bubbleColor
									}}
								>
									{
										!isOwnMessage && (
											<div
												className='chat-sender'
											>
												<strong>
													{message.sender}{isSpectator && ' (spectating)'}
												</strong>
											</div>
										)
									}
									<div
										className='chat-text'
									>
										{message.body}
									</div>
								</div>
							)
						})
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
