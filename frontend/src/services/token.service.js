class TokenService {
    getLocalRefreshToken() {
        const user = JSON.parse(localStorage.getItem('user'))
        return user?.refreshToken
    }

    getLocalAccessToken() {
        const jwtString = localStorage.getItem('jwt')

        if (!jwtString || jwtString === 'undefined') {
            return null 
        }
        try {
            const jwt = JSON.parse(jwtString)
            return jwt 
        } catch (e) {
            console.error('Error al parsear el token JWT:', e)
            return null
        }
    }

    updateLocalAccessToken(token) {
        window.localStorage.setItem('jwt', JSON.stringify(token))
    }

    getUser() {
        return this.getLocalAccessToken() != null ? JSON.parse(localStorage.getItem('user')) : null
    }

    setUser(user) {
        window.localStorage.setItem('user', JSON.stringify(user))
    }

    removeUser() {
        window.localStorage.removeItem('user')
        window.localStorage.removeItem('jwt')
    }
}

const tokenService = new TokenService();

export default tokenService;
