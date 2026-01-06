// TODO: Maybe use dynamic strings for image paths, instead of importing each one
// Since images can be loaded with a route
import blueImage from '../../static/images/cards/CB_BACK.png';
import greenImage from '../../static/images/cards/CG_BACK.png';
import magentaImage from '../../static/images/cards/CM_BACK.png';
import orangeImage from '../../static/images/cards/CO_BACK.png';
import redImage from '../../static/images/cards/CR_BACK.png';
import violetImage from '../../static/images/cards/CV_BACK.png';
import whiteImage from '../../static/images/cards/CW_BACK.png';
import yellowImage from '../../static/images/cards/CY_BACK.png';

export const GameGamePlayerFormInputs = [
    {
        color: 'RED',
        image: redImage,
    },
    {
        color: 'ORANGE',
        image: orangeImage,
    },
    {
        color: 'YELLOW',
        image: yellowImage,
    },
    {
        color: 'GREEN',
        image: greenImage,
    },
    {
        color: 'BLUE',
        image: blueImage,
    },
    {
        color: 'MAGENTA',
        image: magentaImage,
    },
    {
        color: 'VIOLET',
        image: violetImage,
    },
    {
        color: 'WHITE',
        image: whiteImage,
    },
];