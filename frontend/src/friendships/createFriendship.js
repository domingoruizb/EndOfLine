// TODO: Probably should be refactored (200 lines)
// Possibly extract api calls/toasts to a custom file/custom functions to reuse them
// Possibly remove extra api calls since backend could handle them
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import { toast } from "react-toastify";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import "../static/css/friendships/friendsList.css";

export default function FriendshipCreation() {
    const jwt = tokenService.getLocalAccessToken();
    const user = tokenService.getUser();
    const [message] = useState(null);
    const [visible, setVisible] = useState(false);
    const [username, setUsername] = useState("");

    const modal = getErrorModal(setVisible, visible, message);

    const handleChange = (event) => {
        setUsername(event.target.value);
    };

    let navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const idResponse = await fetch(`/api/v1/users/username/${username}`, {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
            });

            if (!idResponse.ok)
                throw new Error(`Player with username ${username} does not exist.`);

            const player = await idResponse.json();
            const friendshipsResponse = await fetch(`/api/v1/friendships/myFriendships`, {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
            });

            const friendshipsJson = await friendshipsResponse.json();
            if (friendshipsJson.some(friendship => friendship.sender.id === player.id || friendship.receiver.id === player.id))
                throw new Error('You are already friends or you have a pending friendship request from this player.');

            const response = await fetch(`/api/v1/friendships`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    sender: user.id,
                    receiver: player.id
                })
            });

            if (!response.ok)
                throw new Error('Failed to send friendship request.');

            toast.success(`🎉 Friendship request sent to ${username}!`, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
            
            setTimeout(() => {
                navigate("/friends");
            }, 500);
        } catch (error) {
            toast.error(`❌ ${error.message}`, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        }
    };


    return (
        <div className="friend-list-page">
            <div className="friend-content-wrapper">
                <div className="create-friendship-container">
                    <h1 className="create-friendship-title">
                        Send Friendship
                    </h1>
                {modal}
                <Form onSubmit={handleSubmit} className="create-friendship-form">
                    <div className="custom-form-input">
                        <Label for="username" className="create-friendship-label">
                            Friend's Username
                        </Label>
                        <Input
                            type="text"
                            required
                            name="username"
                            id="username"
                            value={username}
                            onChange={handleChange}
                            className="create-friendship-input"
                        />
                    </div>
                    <div className="create-friendship-buttons">
                        <button
                            type="button"
                            onClick={() => navigate("/friends")}
                            className="create-friendship-cancel-button"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="create-friendship-send-button"
                        >
                            Send
                        </button>
                    </div>
                </Form>
                </div>
            </div>
        </div>
    );
}
