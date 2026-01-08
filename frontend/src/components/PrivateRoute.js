import { useEffect } from 'react'
import tokenService from '../services/token.service'
import Login from '../auth/login'
import { useFetchResource } from '../util/useFetchResource'

export default function PrivateRoute ({
    children
}) {
    const jwt = tokenService.getLocalAccessToken()
    const { data, loading, getData } = useFetchResource()

    useEffect(() => {
        const validateToken = async () => {
            if (jwt == null) {
                return
            }
            await new Promise(res => setTimeout(res, 2000));
            await getData(
                `/api/v1/auth/validate?token=${jwt}`
            )
        }

        validateToken()
    }, [])

    return loading ? (
        <div className="error-page-container">
            <h1 className="error-title">Loading</h1>
            <h2 className="error-subtitle">Please wait</h2>
        </div>
    ) : (
        data ? (
            children
        ) : (
            <Login />
        )
    )
}
