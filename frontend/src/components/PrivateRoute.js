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

            await getData(
                `/api/v1/auth/validate?token=${jwt}`
            )
        }

        validateToken()
    }, [])

    return loading ? (
        <div>
            Loading...
        </div>
    ) : (
        data ? (
            children
        ) : (
            <Login />
        )
    )
}
