import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Form } from "reactstrap";
import FriendFormInput from "./friendshipUtils/FriendFormInput";
import { showSuccessToast, showErrorToast } from "./friendshipUtils/toastUtils";
import { getUserByUsername, getMyFriendships, sendFriendshipRequest } from "./friendshipUtils/friendshipApi";
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
            const player = await getUserByUsername(username, jwt);
            const friendshipsJson = await getMyFriendships(jwt);
            if (friendshipsJson.some(friendship => friendship.sender.id === player.id || friendship.receiver.id === player.id))
                throw new Error('You are already friends or you have a pending friendship request from this player.');
            await sendFriendshipRequest(user.id, player.id, jwt);
            showSuccessToast(`Friendship request sent to ${username}!`);
            setTimeout(() => {
                navigate("/friends");
            }, 500);
        } catch (error) {
            showErrorToast(error.message);
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
                    <FriendFormInput value={username} onChange={handleChange} />
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
