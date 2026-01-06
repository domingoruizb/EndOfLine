export const COLOR_MAP = {
    RED: '#E31E25',
    ORANGE: '#F39514',
    YELLOW: '#FFEF47',
    GREEN: '#50B15F',
    BLUE: '#00A0E3',
    MAGENTA: '#E5087F',
    VIOLET: '#C48FBF',
    WHITE: '#C5C6C6'
}

export const colorImages = Object.keys(COLOR_MAP).map(color => ({
    color: color,
    image: `/cards/C${color.charAt(0)}_BACK.png`
}))
