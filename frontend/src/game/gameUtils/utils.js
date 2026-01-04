export function getHost (game) {
    return game.players.find(player => player.isHost)
}

export function getGuest (game) {
    return game.players.find(player => !player.isHost)
}

export function isHost (game) {
    return game.userId === getHost(game).userId
}

export function isTurn (game) {
    return game.turn === game.userId
}

export function getTime (elapsed) {
    return {
        seconds: Math.floor(elapsed / 1000) % 60,
        minutes: Math.floor(elapsed / 60000) % 60,
        hours: Math.floor(elapsed / 3600000)
    }
}

export function getColor (color) {
    return color.charAt(0).toUpperCase()
}

export function getOwnColor (game) {
    if (game == null) {
        return 'W'
    }

    const host = getHost(game)
    const isHostPlayer = isHost(game)
    const color = isHostPlayer ? host.color : getGuest(game).color
    return getColor(color)
}

export function getEnergyRotation (game) {
    if (game == null) {
        return 0
    }

    return (3 - game.energy) * 90
}

export function getCardName (card) {
    return card?.image?.split('/')?.pop()?.replace('.png', '');
}

export function getStartingCards (game) {
    if (game == null) return []

    const host = getHost(game)
    const guest = getGuest(game)
    const hostColor = getColor(host.color)
    const guestColor = getColor(guest.color)

    return [
        {
            index: 30,
            card: {
                color: host.color,
                image: `/cardImages/C${hostColor}_START.png`
            }
        },
        {
            index: 32,
            card: {
                color: guest.color,
                image: `/cardImages/C${guestColor}_START.png`
            }
        }
    ]
}

export function getBoard (game) {
    if (game == null) {
        return Array(49).fill(null)
    }

    const board = Array(49).fill(null)
    const startingCards = getStartingCards(game)

    for (const { index, card } of startingCards) {
        board[index] = {
            index,
            rotation: 0,
            card
        }
    }

    return board
}
