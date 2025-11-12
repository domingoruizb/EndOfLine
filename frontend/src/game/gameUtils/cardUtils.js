export const initialPositions = [[2, 4], [4, 4]]
export const boardLength = 7
export const boardArray = Array(boardLength * boardLength).fill(null).map((_, index) => {
    const row = index % boardLength
    const col = Math.floor(index / boardLength)
    return initialPositions.some(pos => pos[0] === row && pos[1] === col) ? {
        name: 'START',
        rotation: 0
    } : null
})

// X,Y to Index
export function getIndex (row, col) {
    return row + col * boardLength
}

// Index to X,Y
export function getCoordinates (index) {
    const row = index % boardLength
    const col = Math.floor(index / boardLength)
    return {
        row,
        col
    }
}

export function getCards (cards) {
    const shuffled = cards.sort(() => 0.5 - Math.random())
    return shuffled.slice(0, 5)
}

export function nameToBinary (cardName) {
    return parseInt(cardName.split('_')[1], 2)
}

export function getCardName (card) {
    return card?.image?.split('/')?.pop()?.replace('.png', '');
}
