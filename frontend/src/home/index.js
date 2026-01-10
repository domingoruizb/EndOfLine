import tokenService from '../services/token.service'
import LinkClickButton from '../components/LinkClickButton'
import '../static/css/page.css'
import '../static/css/home/home.css'

export default function Home () {
    const user = tokenService.getUser()
    const isAdmin = user?.roles.includes('ADMIN')

    return(
        <div
            className='page-container'
        >
            <div
                className='home-container'
            >
                <img
                    src='/images/logo_transparent.png'
                    alt='Logo'
                />
                <div
                    className='buttons-container'
                >
                    {
                        user == null ? (
                            <>
                                <LinkClickButton
                                    text='REGISTER'
                                    link='/register'
                                />
                                <LinkClickButton
                                    text='LOGIN'
                                    link='/login'
                                    className='orange'
                                />
                            </>
                        ) : (
                            isAdmin ? (
                                <>
                                    <LinkClickButton
                                        link='/games'
                                        text='GAMES'
                                    />
                                    <LinkClickButton
                                        link='/users'
                                        text='USERS'
                                        className='orange'
                                    />
                                    <LinkClickButton
                                        link='/social'
                                        text='SOCIAL'
                                        className='orange'
                                    />
                                    <LinkClickButton
                                        link='/achievements'
                                        text='ACHIEVEMENTS'
                                    />
                                </>
                            ) : (
                                <LinkClickButton
                                    link='/creategame'
                                    text='PLAY'
                                />
                            )
                        )
                    }
                </div>
            </div>
        </div>
    )
}