import { useRef, useState } from 'react'
import { toast } from 'react-toastify'
import tokenService from '../services/token.service'

const jwt = tokenService.getLocalAccessToken()

export function useFetchResource () {
    const [data, setData] = useState(null)
    const [loading, setLoading] = useState(true)
    const controllerRef = useRef(null)

    const getData = async (url, method = 'GET', body = null, options = {}) => {
        if (controllerRef.current != null) {
            controllerRef.current.abort()
        }

        const abortController = new AbortController()
        controllerRef.current = abortController
        setLoading(true)

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`,
                    ...options.headers
                },
                body: body == null ? null : JSON.stringify(body),
                signal: abortController.signal,
                ...options
            })

            const text = await response.text()
            const json = text === '' ? null : JSON.parse(text)

            if (!response.ok) {
                const message = json?.message || json?.error || `Request failed: ${response.status}`
                throw new Error(message)
            }

            setData(json)
            return json
        } catch (error) {
            if (error.name !== 'AbortError') {
                toast.error(`❌ ${error.message ?? 'Something went wrong'}`)
            }
        } finally {
            if (!abortController.signal.aborted) {
                setLoading(false)
            }
        }
    }

    return {
        data,
        loading,
        getData
    }
}
