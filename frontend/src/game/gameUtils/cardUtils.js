const exampleCards = [
    'Card_0010',
    'Card_0100',
    'Card_0110',
    'Card_1000',
    'Card_1010',
    'Card_1100',
    'Card_1110'
]

const initialPositions = [[2, 4], [4, 4]]
const boardLength = 7
const boardArray = Array(boardLength * boardLength).fill(null).map((_, index) => {
    const row = index % boardLength
    const col = Math.floor(index / boardLength)
    return initialPositions.some(pos => pos[0] === row && pos[1] === col) ? {
        name: 'START',
        rotation: 0
    } : null
})

function getInitialValidIndexes (isHost) {
    const positions = initialPositions
    const startPos = isHost ? positions[0] : positions[1]
    return [
        toIndex(startPos[0], startPos[1] - 1)
    ]
}

function getCoordinates (index) {
    const row = index % boardLength
    const col = Math.floor(index / boardLength)
    return {
        row,
        col
    }
}

function getCards (cards) {
    const shuffled = cards.sort(() => 0.5 - Math.random())
    return shuffled.slice(0, 5)
}

function nameToBinary (cardName) {
    return parseInt(cardName.split('_')[1], 2)
}

function getRotation (index, lastPlaced) {
    if (!lastPlaced || lastPlaced.name === 'START') {
        return 0
    }

    const selectedPos = getCoordinates(index)
    const lastPos = getCoordinates(lastPlaced.index)

    // Module boardLength to handle wrapping
    const rowDelta = (selectedPos.row - lastPos.row + boardLength) % boardLength
    const colDelta = (selectedPos.col - lastPos.col + boardLength) % boardLength

    if (rowDelta === 1 && colDelta === 0) {
        // Right
        return -3
    } else if (rowDelta === boardLength - 1 && colDelta === 0) {
        // Left (including wrap)
        return -1
    } else if (rowDelta === 0 && colDelta === 1) {
        // Down
        return -2
    } else if (rowDelta === 0 && colDelta === boardLength - 1) {
        // Up (including wrap)
        return 0
    }

    return 0
}

function rotateBits (bits, rotation) {
    const absRot = Math.abs(rotation)
    return (((bits << absRot) | (bits >>> (4 - absRot))) >>> 0) & 0b1111
}

// X,Y to Index
function toIndex (row, col) {
    return row + col * boardLength
}

function getValidIndexes (lastPlacedCard, board) {
    if (!lastPlacedCard || lastPlacedCard.name === 'START') {
        return []
    }

    const { row, col } = getCoordinates(lastPlacedCard.index)

    // Get valid directions
    const prevBits = nameToBinary(lastPlacedCard.name)
    const rotatedPrevBits = rotateBits(prevBits, lastPlacedCard.rotation)

    const dirs = {
        left: rotatedPrevBits & 0b1000, // 1: Left
        up: rotatedPrevBits & 0b0100, // 2: Up
        right: rotatedPrevBits & 0b0010, // 4: Right
        down: rotatedPrevBits & 0b0001  // 8: Down
    }

    const potential = []

    if (dirs.left) {
        const newRow = (row - 1 + boardLength) % boardLength
        potential.push(toIndex(newRow, col))
    }

    if (dirs.right) {
        const newRow = (row + 1) % boardLength
        potential.push(toIndex(newRow, col))
    }
    if (dirs.up) {
        const newCol = (col - 1 + boardLength) % boardLength
        potential.push(toIndex(row, newCol))
    }

    if (dirs.down) {
        const newCol = (col + 1) % boardLength
        potential.push(toIndex(row, newCol))
    }

    return potential.filter(idx => board[idx] == null)
}

function checkPlacementValid (board, selectedCard, index, lastPlacedCard) {
    if (selectedCard == null) {
        return false
    }

    const coords = getCoordinates(index)
    const isStart = initialPositions.some(p => p[0] === coords.row && p[1] === coords.col)

    if (lastPlacedCard != null) {
        const validIndexes = getValidIndexes(lastPlacedCard, board)

        if (!validIndexes.includes(index)) {
            return false
        }
    }

    return selectedCard != null && !isStart && board[index] == null
}

export {
    exampleCards,
    initialPositions,
    boardLength,
    boardArray,
    getInitialValidIndexes,
    getCards,
    getCoordinates,
    nameToBinary,
    checkPlacementValid,
    getRotation,
    getValidIndexes
}
