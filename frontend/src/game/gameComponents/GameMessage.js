import tokenService from '../../services/token.service'
import { COLOR_MAP } from '../gameUtils/colors'

const user = tokenService.getUser()

export default function GameMessage ({
    game,
    message
}) {
    const isOwnMessage = message.sender === user.username
    const player = game.players.find(p => p.username === message.sender)
    const isSpectator = player == null
    const bubbleColor = isOwnMessage ? (
        'var(--main-orange-color)'
    ) : (
        isSpectator ? COLOR_MAP.WHITE : (COLOR_MAP[player.color] ?? '#2ECC71')
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
                    <span
                        className='chat-sender'
                    >
                        {message.sender}{isSpectator && ' (SPECTATOR)'}
                    </span>
                )
            }
            <span
                className='chat-text'
            >
                {message.body}
            </span>
            <span
                className='chat-timestamp'
            >
                {
                    new Date(message.sentAt).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit'
                    })
                }
            </span>
        </div>
    )
}
