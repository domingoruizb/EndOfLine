import { Link } from 'react-router-dom'
import '../static/css/button.css'

export default function Button ({
    text,
    link,
    onClick,
    className
}) {
    return onClick == null ? (
        <Link
            to={link}
            className={`button ${className}`}
        >
            {text}
        </Link>
    ) : (
        <button
            onClick={onClick}
            className={`button ${className}`}
        >
            {text}
        </button>
    )
}
