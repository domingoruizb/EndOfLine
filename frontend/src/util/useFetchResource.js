import { useEffect, useRef, useState } from 'react'
import { showErrorToast } from './toasts'
import tokenService from '../services/token.service'

export function useFetchResource () {
    const [data, setData] = useState(null)
    const [status, setStatus] = useState('idle') // 'idle' | 'loading' | 'success' | 'error'
    const controllerRef = useRef(null)

    const getData = async (url, method = 'GET', body = null, options = {}) => {
        if (controllerRef.current != null) {
            controllerRef.current.abort()
        }

        const abortController = new AbortController()
        controllerRef.current = abortController

        setStatus('loading')
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
                const message = json?.message || json?.error || `Request failed: ${response.status} ${response.statusText}`
                setStatus('error')
                throw new Error(message)
            }

            setData(json)
            setStatus('success')
            return {
                status: 'success',
                data: json
            }
        } catch (error) {
            if (error.name === 'AbortError') {
                return {
                    status: 'error',
                    error: error.message
                }
            }

            showErrorToast(error.message ?? 'Something went wrong')
            setStatus('error')
            return {
                status: 'error',
                error: error.message
            }
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
        status,
        getData
    }
}
