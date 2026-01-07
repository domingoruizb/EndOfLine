import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

import FriendTableRow from "./friendshipUtils/FriendTableRow";
import FriendPagination from "./FriendPagination";
import { filterAndSortFriendships, paginate } from "./friendshipUtils/friendshipListUtils";
import { showSuccessToast, showErrorToast } from "./friendshipUtils/toastUtils";
import useFriendshipNotifications from "./friendshipUtils/useFriendshipNotifications";
import { getMyFriendships } from "./friendshipUtils/friendshipApi";
import { acceptFriendshipApi, rejectFriendshipApi } from "./friendshipUtils/friendshipApi";
import tokenService from "../services/token.service";
import deleteFromList from "../util/deleteFromList";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import "../static/css/friendships/friendsList.css";

export default function FriendshipList() {
    const jwt = tokenService.getLocalAccessToken();
    const user = tokenService.getUser();
    const [friendshipType, setFriendshipType] = useState("ACCEPTED");
    const [friendships, setFriendships] = useFetchState(null, `/api/v1/friendships/myFriendships`, jwt);
    const [activeGames] = useFetchState([], `/api/v1/games`, jwt);
    const [currentPage, setCurrentPage] = useState(1);
    const [friendshipsPerPage] = useState(5);

    const [message, setMessage] = useState("");
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);

    let navigate = useNavigate();

    const handleClick = () => {
        navigate("/friendships/create");
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await getMyFriendships(jwt);
                setFriendships(data);
            } catch (error) {
                showErrorToast("Error fetching friendships data");
                setMessage("Error fetching friendships data");
                setVisible(true);
            }
        };
        fetchData();
    }, [jwt, user.id, friendshipType, setFriendships]);

    useFriendshipNotifications(friendships, user, setFriendships, jwt);


    const sortedFriendships = filterAndSortFriendships(friendships, friendshipType, user.id);
    const currentFriendships = paginate(sortedFriendships, currentPage, friendshipsPerPage);

    const modal = getErrorModal(setVisible, visible, message);

    const acceptFriendship = async (friendshipId) => {
        try {
            await acceptFriendshipApi(friendshipId, jwt);
            const acceptedFriendship = friendships.find(f => f.id === friendshipId);
            setFriendships(friendships.filter(friendship => friendship.id !== friendshipId));
            showSuccessToast(`Friendship with ${acceptedFriendship.sender.username} accepted!`);
        } catch (error) {
            showErrorToast(error.message);
            setMessage(`Error: ${error}`);
            setVisible(true);
        }
    };

    const rejectFriendship = async (friendshipId) => {
        try {
            await rejectFriendshipApi(friendshipId, jwt);
            const rejectedFriendship = friendships.find(f => f.id === friendshipId);
            setFriendships(friendships.filter(friendship => friendship.id !== friendshipId));
            showSuccessToast(`Friendship request from ${rejectedFriendship.sender.username} rejected.`);
        } catch (error) {
            showErrorToast(error.message);
            setMessage(`Error: ${error}`);
            setVisible(true);
        }
    };

    const getFriendActiveGame = (friendUserId) => {
        if (!activeGames || activeGames.length === 0) return null;
        return activeGames.find(game => 
            game.endedAt === null && 
            game.gamePlayers?.some(gp => gp.user.id === friendUserId)
        );
    };

    const handleSpectateGame = (gameId) => {
        navigate(`/game/${gameId}`);
    };



    const totalPages = Math.ceil(sortedFriendships.length / friendshipsPerPage);

    return (
        <div className="friend-list-page">
            <div className="friend-content-wrapper">
            <div className="admin-page-container-friend">
                <h1 className="friend-list-title">
                    {friendshipType === "ACCEPTED" ? "Friendships" : "Pending Invites"}
                </h1>
                {modal}
                {currentFriendships.length > 0 ? (
                    <>
                        <table aria-label="friendships" className="friend-table mt-4 text-white">
                            <thead>
                                <tr>
                                    <th className="text-center">Username</th>
                                    <th className="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {currentFriendships.map((friendship) => (
                                    <FriendTableRow
                                        key={friendship.id}
                                        friendship={friendship}
                                        user={user}
                                        friendshipType={friendshipType}
                                        onAccept={acceptFriendship}
                                        onReject={rejectFriendship}
                                        onSpectate={handleSpectateGame}
                                        onDelete={(id) => deleteFromList(
                                            `/api/v1/friendships/${id}`,
                                            id,
                                            [friendships, setFriendships],
                                            [alerts, setAlerts],
                                            setMessage,
                                            setVisible
                                        )}
                                        canSpectate={!!getFriendActiveGame((friendship.sender.id === user.id ? friendship.receiver : friendship.sender).id)}
                                        getFriendActiveGame={getFriendActiveGame}
                                    />
                                ))}
                            </tbody>
                        </table>
                        {totalPages > 1 && (
                            <FriendPagination currentPage={currentPage} totalPages={totalPages} setCurrentPage={setCurrentPage} />
                        )}
                    </>
                ) : (
                    <p className="text-center text-white mt-4">
                        {friendshipType === "ACCEPTED" 
                            ? "You don't have any friends yet." 
                            : "You don't have any pending friend invitations yet."
                        }
                    </p>
                )}
                <div className="friend-cta-row text-center mt-4">
                    <button
                        onClick={() => {
                            setFriendshipType(friendshipType === "PENDING" ? "ACCEPTED" : "PENDING");
                            setCurrentPage(1);
                        }}
                        className="friend-cta-button"
                    >
                        {friendshipType === "PENDING" ? "Show Friendships" : "Show Pending"}
                    </button>
                    <button
                        onClick={handleClick}
                        className="friend-cta-button friend-add-button"
                    >
                        Add Friend
                    </button>
                </div>
            </div>
            </div>
        </div>
    );
};