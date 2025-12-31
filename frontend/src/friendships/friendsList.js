import React, { useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "reactstrap";
import { toast } from "react-toastify";
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
    const [activeGames, setActiveGames] = useFetchState([], `/api/v1/games`, jwt);
    const [currentPage, setCurrentPage] = useState(1);
    const [friendshipsPerPage] = useState(5);
    const notifiedPendingRef = useRef(new Set());

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
                const response = await fetch(`/api/v1/friendships/myFriendships`, {
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                    },
                });
                const data = await response.json();
                setFriendships(data);

                const newPendingRequests = data.filter(
                    (f) => f.friendState === "PENDING" && f.receiver.id === user.id
                );

                newPendingRequests.forEach((request) => {
                    if (!notifiedPendingRef.current.has(request.id)) {
                        toast.info(`👤 ${request.sender.username} sent you a friendship request!`, {
                            position: "top-right",
                            autoClose: 4000,
                            hideProgressBar: false,
                            closeOnClick: true,
                            pauseOnHover: true,
                            draggable: true,
                        });
                        notifiedPendingRef.current.add(request.id);
                    }
                });
            } catch (error) {
                setMessage("Error fetching friendships data");
                setVisible(true);
            }
        };
        fetchData();
    }, [jwt, user.id, friendshipType, setFriendships]);

    const filteredFriendships = friendships ? friendships.filter(f => f.friendState === friendshipType) : [];

    const sortedFriendships = [...filteredFriendships].sort((a, b) => {
        const isAReceiver = a.receiver.id === user.id;
        const isBReceiver = b.receiver.id === user.id;
        if (isAReceiver && !isBReceiver && a.friendState === "PENDING")
            return -1;
        else if (!isAReceiver && isBReceiver && b.friendState === "PENDING")
            return 1;
        else
            return 0;
    });

    const indexOfLastFriendship = currentPage * friendshipsPerPage;
    const indexOfFirstFriendship = indexOfLastFriendship - friendshipsPerPage;
    const currentFriendships = sortedFriendships.slice(indexOfFirstFriendship, indexOfLastFriendship);
    const paginate = pageNumber => setCurrentPage(pageNumber);

    const modal = getErrorModal(setVisible, visible, message);

    const acceptFriendship = async (friendshipId) => {
        try {
            const response = await fetch(`/api/v1/friendships/${friendshipId}/acceptFriendship`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`,
                }
            })
            if (response.ok) {
                const acceptedFriendship = friendships.find(f => f.id === friendshipId);
                setFriendships(friendships.filter(friendship => friendship.id !== friendshipId));
                toast.success(`✅ Friendship with ${acceptedFriendship.sender.username} accepted!`, {
                    position: "top-right",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                });
                } else {
                    throw new Error('Failed to accept the friendship.');
                }
        } catch (error) {
            setMessage(`Error: ${error}`);
            setVisible(true);
        }
    };

    const rejectFriendship = async (friendshipId) => {
        try {
            const response = await fetch(`/api/v1/friendships/${friendshipId}/rejectFriendship`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`,
                }
            })
            if (response.ok) {
                const rejectedFriendship = friendships.find(f => f.id === friendshipId);
                setFriendships(friendships.filter(friendship => friendship.id !== friendshipId));
                toast.info(`🚫 Friendship request from ${rejectedFriendship.sender.username} rejected.`, {
                    position: "top-right",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                });
                } else {
                    throw new Error('Failed to accept the friendship.');
                }
        } catch (error) {
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



    const totalPages = Math.ceil(filteredFriendships.length / friendshipsPerPage);

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
                                {currentFriendships.map((friendship) => {
                                        const isSender = friendship.sender.id === user.id;
                                        const otherUser = isSender ? friendship.receiver : friendship.sender;
                                        return (
                                            <tr key={friendship.id}>
                                                <td className="text-center">{otherUser.username}</td>
                                                <td className="text-center">
                                                    {friendshipType === "PENDING" && !isSender ? (
                                                        <div className="friend-action-group">
                                                            <Button
                                                                aria-label={"update-" + friendship.id}
                                                                size="sm"
                                                                className="positive-button"
                                                                onClick={() => acceptFriendship(friendship.id)}
                                                            >
                                                                Accept
                                                            </Button>
                                                            <Button
                                                                aria-label={"delete-" + friendship.id}
                                                                size="sm"
                                                                className="negative-button"
                                                                onClick={() => rejectFriendship(friendship.id)}
                                                            >
                                                                Reject
                                                            </Button>
                                                        </div>
                                                    ) : (
                                                        <div className="friend-action-group">
                                                            {getFriendActiveGame(otherUser.id) && (
                                                                <Button
                                                                    aria-label={"spectate-" + friendship.id}
                                                                    size="sm"
                                                                    className="positive-button"
                                                                    onClick={() => handleSpectateGame(getFriendActiveGame(otherUser.id).id)}
                                                                >
                                                                    Spectate
                                                                </Button>
                                                            )}
                                                            <Button
                                                                aria-label={"delete-" + friendship.id}
                                                                size="sm"
                                                                className="negative-button"
                                                                onClick={() => deleteFromList(
                                                                    `/api/v1/friendships/${friendship.id}`,
                                                                    friendship.id,
                                                                    [friendships, setFriendships],
                                                                    [alerts, setAlerts],
                                                                    setMessage,
                                                                    setVisible
                                                                )}
                                                            >
                                                                Delete
                                                            </Button>
                                                        </div>
                                                    )}
                                                </td>
                                            </tr>
                                        );
                                    })}
                            </tbody>
                        </table>
                        {totalPages > 1 && (
                            <div className="friend-pagination text-center mt-4">
                                <button
                                    onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                                    disabled={currentPage === 1}
                                    className="friend-pagination-button"
                                >
                                    Previous
                                </button>
                                <span className="friend-pagination-info">
                                    Page {currentPage} of {totalPages}
                                </span>
                                <button
                                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                                    disabled={currentPage === totalPages}
                                    className="friend-pagination-button"
                                >
                                    Next
                                </button>
                            </div>
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