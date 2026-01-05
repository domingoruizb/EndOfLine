import { getEnergyRotation, getOwnColor } from '../gameUtils/utils'

export default function EnergyCard ({
    game
}) {
    return !game.spectating && (
        <img
            src={`/cardImages/C${getOwnColor(game)}_ENERGY.png`}
            alt='Energy Symbol'
            className='card-button'
            style={{
                rotate: getEnergyRotation(game) + 'deg'
            }}
        />
    )
}
