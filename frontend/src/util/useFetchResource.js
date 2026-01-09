import { useEffect, useRef, useState } from 'react'
import { showErrorToast } from './toasts'
import tokenService from '../services/token.service'

export function useFetchResource () {
    const [data, setData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [success, setSuccess] = useState(null)
    const controllerRef = useRef(null)

    const getData = async (url, method = 'GET', body = null, options = {}) => {
        if (controllerRef.current != null) {
            controllerRef.current.abort()
        }

        const abortController = new AbortController()
        controllerRef.current = abortController

        setLoading(true)
        setSuccess(null)

        try {
            const jwt = tokenService.getLocalAccessToken()
            const response = await fetch(url, {
                method,
                headers: {
                    ...options.headers,
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`
                },
                body: body == null ? null : JSON.stringify(body),
                signal: abortController.signal,
                ...options
            })

            const text = await response.text()
            const json = text === '' ? null : JSON.parse(text)

            if (!response.ok) {
                const message = json?.message || json?.error || `Request failed: ${response.status}`
                setSuccess(false)
                throw new Error(message)
            }

            setData(json)
            setSuccess(true)
            return {
                success: true,
                data: json
            }
        } catch (error) {
            if (error.name !== 'AbortError') {
                showErrorToast(error.message ?? 'Something went wrong')
            }
            setSuccess(false)
            return {
                success: false,
                error: error.message
            }
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        return () => {
            if (controllerRef.current != null) {
                controllerRef.current.abort()
            }
        }
    }, [])

    return {
        data,
        loading,
        success,
        getData
    }
}
