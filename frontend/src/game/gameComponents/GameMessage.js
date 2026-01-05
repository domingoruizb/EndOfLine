import tokenService from '../../services/token.service'

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
        </div>
    )
}
