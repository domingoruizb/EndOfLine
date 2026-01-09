import React, { useEffect, useRef } from "react";
import { Route, Routes } from "react-router-dom";
import jwt_decode from "jwt-decode";
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
import { toast } from "react-toastify";
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

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

function App() {
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();
  let roles = []
  if (jwt) {
    roles = getRolesFromJWT(jwt);
  }

  const notifiedPendingRef = useRef(new Set());

  useEffect(() => {
    if (!jwt || !user?.id) return;
      const isAdmin = (
        user?.roles[0] === "ADMIN"
      );

    const controller = new AbortController();

    const fetchPending = async () => {
      try {
        if (!isAdmin) {
          const response = await fetch(`/api/v1/friendships/myFriendships`, {
          headers: { Authorization: `Bearer ${jwt}` },
          signal: controller.signal,
        });
        if (!response.ok) return;
        const data = await response.json();
        const newPending = data.filter(
          (f) => f.friendState === "PENDING" && f.receiver?.id === user.id
          );
        newPending.forEach((req) => {
          if (!notifiedPendingRef.current.has(req.id)) {
            toast.info(`👤 ${req.sender.username} sent you a friendship request!`, {
              position: "top-right",
              autoClose: 4000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
            });
            notifiedPendingRef.current.add(req.id);
          }
        });
      }
      } catch (err) {
        if (err.name !== 'AbortError') {
        }
      }
    };

    fetchPending();
    const interval = setInterval(fetchPending, 30000); // 30s polling
    return () => {
      controller.abort();
      clearInterval(interval);
    };
  }, [jwt, user?.id]);

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  let adminRoutes = <></>;
  let ownerRoutes = <></>;
  let userRoutes = <></>;
  let vetRoutes = <></>;
  let publicRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
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
        </>)
    }
    if (role === "PLAYER") {
      ownerRoutes = (
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
        </>)
    }    
  })
  if (!jwt) {
    publicRoutes = (
      <>        
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </>
    )
  } else {
    userRoutes = (
      <>
        {/* <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} /> */}        
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
        <Route path="/myprofile" element={<PrivateRoute><MyProfile /></PrivateRoute>} />
      </>
    )
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/rules" element={<RulesPage />} />
          <Route path="/stats" element={<UserStats />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
          {ownerRoutes}
          {vetRoutes}
        {/* Catch-all 404 route */}
        <Route path="*" element={<ErrorPage />} />
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;
