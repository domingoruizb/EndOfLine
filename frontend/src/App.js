import { useEffect, useRef } from "react";
import { Route, Routes } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";
import AppNavbar from "./AppNavbar";
import Home from "./home";
import PrivateRoute from "./components/PrivateRoute";
import Register from "./auth/register";
import Login from "./auth/login";
import Logout from "./auth/logout";
import RulesPage from "./rules";
import tokenService from "./services/token.service";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import SwaggerDocs from "./swagger";
import DeveloperList from "./developers";
import AchievementList from "./achievements/achievementList";
import AchievementEdit from './achievements/achievementEdit';
import AchievementCreate from './achievements/achievementCreate';
import UserStats from './stats/UserStats';
import MyProfile from './myprofile/myProfile';
import Friends from './friendships/friendsList';
import FriendshipCreation from './friendships/createFriendship'
import GamePage from './game';
import CreateGame from './lobby/createGame';
import LobbyGame from './lobby/lobbyGame';
import JoinGame from './lobby/joinGame';
import PlayerGamesList from "./games/playerGames";
import AdminGamesList from "./games/adminGames";
import Social from "./social/Social";
import ErrorPage from "./error-page";
import { useFetchResource } from './util/useFetchResource';
import { showInfoToast } from './util/toasts';

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

export default function App() {
  const { getData } = useFetchResource()
  const notified = useRef(new Set())

  const user = tokenService.getUser()
  const isAdmin = user?.roles.includes("ADMIN")

  useEffect(() => {
    const fetchFriendships = async () => {
      if (isAdmin || user == null) return

      const { data, success } = await getData('/api/v1/friendships/pending')
      if (!success) return

      const pending = data.filter(f => !notified.current.has(f.id))
      pending.forEach(req => {
        showInfoToast(`👤 ${req.sender.username} sent you a friendship request!`)
        notified.current.add(req.id)
      })
    }

    fetchFriendships()
    const interval = setInterval(fetchFriendships, 30000)
    return () => {
      clearInterval(interval)
    }
  }, [])

  const roleRoutes = user?.roles.includes("ADMIN") ? (
    <>
      <Route path="/users" exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
      <Route path="/users/:userId" exact={true} element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
      <Route path="/developers" element={<DeveloperList />} />
      <Route path="/games" element={<PrivateRoute><AdminGamesList /></PrivateRoute>} />
      <Route path="/game/:gameId" exact={true} element={<PrivateRoute><GamePage /></PrivateRoute>} />
      <Route path="/achievements" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
      <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit /></PrivateRoute>} />
      <Route path="/achievements/new" exact={true} element={<PrivateRoute><AchievementCreate /></PrivateRoute>} />
      <Route path="/social" exact={true} element={<PrivateRoute><Social /></PrivateRoute>} />
    </>
  ) : (
    user?.roles.includes("PLAYER") ? (
      <>
        <Route path="/creategame" exact={true} element={<PrivateRoute><CreateGame /></PrivateRoute>} />
        <Route path="/lobby/:gameId" exact={true} element={<PrivateRoute><LobbyGame /></PrivateRoute>} />
        <Route path="/joingame" exact={true} element={<PrivateRoute><JoinGame /></PrivateRoute>} />
        <Route path="/achievements" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
        <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit /></PrivateRoute>} />
        <Route path="/games" element={<PrivateRoute><PlayerGamesList /></PrivateRoute>} />
        <Route path="/friends" exact={true} element={<PrivateRoute><Friends /></PrivateRoute>} />
        <Route path="/friendships/create" exact={true} element={<PrivateRoute><FriendshipCreation /></PrivateRoute>} />
        <Route path="/game/:gameId" exact={true} element={<PrivateRoute><GamePage /></PrivateRoute>} />
        <Route path="/social" exact={true} element={<PrivateRoute><Social /></PrivateRoute>} />
      </>
    ) : <></>
  )

  const authRoutes = user == null ? (
    <>
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
    </>
  ) : (
    <>
      <Route path="/logout" element={<Logout />} />
      <Route path="/login" element={<Login />} />
      <Route path="/myprofile" element={<PrivateRoute><MyProfile /></PrivateRoute>} />
    </>
  )

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/rules" element={<RulesPage />} />
          <Route path="/stats" element={<UserStats />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {authRoutes}
          {roleRoutes}
          {/* Catch-all 404 route */}
          <Route path="*" element={<ErrorPage />} />
        </Routes>
      </ErrorBoundary>
    </div>
  );
}
