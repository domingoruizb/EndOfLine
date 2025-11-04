import { useState } from 'react'
import './game.css'

const exampleCards = [
    'Card1',
    'Card2',
    'Card3',
    'Card4',
    'Card5'
]

const skills = [
    'Boost',
    'Brake',
    'Extra Gas',
    'Reverse'
]

export default function GamePage () {
    const [selectedCard, setSelectedCard] = useState(null)
    const [board, setBoard] = useState(Array(49).fill(null))

    const handleCardSelect = (card) => {
        setSelectedCard(card)
        console.log(`Selected card: ${card}`)
    }

    const handlePlaceCard = (index) => {
        if (!selectedCard) {
            console.log('No card selected')
            return
        }

        console.log(`Placed card: ${selectedCard} at index: ${index}`)
        setBoard(prevBoard => {
            const newBoard = [...prevBoard]
            newBoard[index] = selectedCard
            return newBoard
        })
        setSelectedCard(null)
    }

    const getCoordinates = (index) => {
        const row = index % 7
        const col = Math.floor(index / 7)
        return {
            row,
            col
        }
    }

    return (
        <div
            className='game-page-container'
        >
            <div
                className='side-container'
            >
                <div
                    className='skills-container'
                >
                    {
                        skills.map((skill) => (
                            <button
                                key={skill}
                                className='skill-button'
                                onClick={() => console.log(`Activated skill: ${skill}`)}
                            >
                                {skill}
                            </button>
                        ))
                    }
                </div>
                <div
                    className='cards-container'
                >
                    {
                        exampleCards.map((card) => (
                            <button
                                key={card}
                                onClick={() => handleCardSelect(card)}
                                className='card-button'
                                style={{
                                    border: selectedCard === card && '5px solid yellow'
                                }}
                            >
                                {card.toUpperCase()}
                            </button>
                        ))
                    }
                    <p
                        style={{
                            color: 'white'
                        }}
                    >
                        Select a card
                    </p>
                </div>
            </div>
            <div
                className='game-board-container'
            >
                <div
                    className='game-board'
                >
                    {
                        board.map((card, index) => (
                            <button
                                key={index}
                                className='game-cell'
                                onClick={() => handlePlaceCard(index)}
                            >
                                {
                                    card ? card.toUpperCase() : (
                                        <span
                                            style={{
                                                color: 'gray',
                                            }}
                                        >
                                            ({getCoordinates(index).row}, {getCoordinates(index).col})
                                        </span>
                                    )
                                }
                            </button>
                        ))
                    }
                </div>
            </div> 
        </div>
    )
}
