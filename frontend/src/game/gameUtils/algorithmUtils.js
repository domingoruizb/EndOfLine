import { initialPositions, boardLength, getCoordinates, getIndex, nameToBinary } from './cardUtils.js'

export function getInitialValidIndexes (isHost) {
    const positions = initialPositions
    const startPos = isHost ? positions[0] : positions[1]
    return [
        getIndex(startPos[0], startPos[1] - 1)
    ]
}

export function getRotation (index, lastPlaced) {
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

export function rotateBits (bits, rotation) {
    const absRot = Math.abs(rotation)
    return (((bits << absRot) | (bits >>> (4 - absRot))) >>> 0) & 0b1111
}

export function getValidIndexes (lastPlacedCard, board) {
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
        potential.push(getIndex(newRow, col))
    }

    if (dirs.right) {
        const newRow = (row + 1) % boardLength
        potential.push(getIndex(newRow, col))
    }
    if (dirs.up) {
        const newCol = (col - 1 + boardLength) % boardLength
        potential.push(getIndex(row, newCol))
    }

    if (dirs.down) {
        const newCol = (col + 1) % boardLength
        potential.push(getIndex(row, newCol))
    }

    return potential.filter(idx => board[idx] == null)
}

export function checkPlacementValid (board, selectedCard, index, lastPlacedCards, isHost) {
    if (selectedCard == null) {
        return false
    }

    if (lastPlacedCards.length === 0) {
        if (isHost == null) {
            return
        }

        if (!getInitialValidIndexes(isHost).includes(index)) {
            return
        }
    }

    const lastPlacedCard = lastPlacedCards[lastPlacedCards.length - 1]

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
