import { Link } from 'react-router-dom'
import '../static/css/button.css'

export default function Button ({
    text,
    link,
    onClick,
    className,
    ...props
}) {
    return onClick == null ? (
        <Link
            to={link}
            className={`button ${className}`}
            {...props}
        >
            {text}
        </Link>
    ) : (
        <button
            onClick={onClick}
            className={`button ${className}`}
            {...props}
        >
            {text}
        </button>
    )
}
