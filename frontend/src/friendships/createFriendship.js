
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { showSuccessToast, showErrorToast } from "./friendshipUtils/toastUtils";
import { getUserByUsername, getMyFriendships, sendFriendshipRequest } from "./friendshipUtils/friendshipApi";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import FormGenerator from "../components/formGenerator/formGenerator";
import createFriendshipInputs from "./friendshipComponents/createFriendshipInputs";
import "../static/css/friendships/friendsList.css";

export default function FriendshipCreation() {
    const jwt = tokenService.getLocalAccessToken();
    const user = tokenService.getUser();
    const [message] = useState(null);
    const [visible, setVisible] = useState(false);

    const [username, setUsername] = useState("");

    const modal = getErrorModal(setVisible, visible, message);



    let navigate = useNavigate();


    const handleSubmit = async ({ values }) => {
        const usernameValue = values.username;
        try {
            const player = await getUserByUsername(usernameValue, jwt);
            const friendshipsJson = await getMyFriendships(jwt);
            if (friendshipsJson.some(friendship => friendship.sender.id === player.id || friendship.receiver.id === player.id))
                throw new Error('You are already friends or you have a pending friendship request from this player.');
            await sendFriendshipRequest(user.id, player.id, jwt);
            showSuccessToast(`Friendship request sent to ${usernameValue}!`);
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
                    <FormGenerator
                        inputs={createFriendshipInputs(username)}
                        onSubmit={handleSubmit}
                        numberOfColumns={1}
                        buttonText="Send"
                        buttonClassName="create-friendship-send-button"
                        childrenPosition={-1}
                    >
                        <button
                            type="button"
                            onClick={() => navigate("/friends")}
                            className="create-friendship-cancel-button"
                            style={{ marginLeft: '1rem', marginBottom: '85px' }}
                        >
                            Cancel
                        </button>
                    </FormGenerator>
                </div>
            </div>
        </div>
    );
}
