import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Form, Input, Label } from "reactstrap";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";

export default function FriendshipCreation() {
    const jwt = tokenService.getLocalAccessToken();
    const user = tokenService.getUser();
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [username, setUsername] = useState("");
    const [error, setError] = useState("");

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

            navigate("/friends");
        } catch (error) {
            setMessage(error.message);
        } finally {
            setVisible(true);
        }
    };


    return (
        <div className="home-page-container">
            <div className="hero-div">
                <h1 className="text-center" style={{
                fontWeight: 800,
                letterSpacing: "2px",
                color: "#FE5B02",
                textShadow: "0 2px 8px #000"
                }}>Send Friendship</h1>
                {modal}
                <Form onSubmit={handleSubmit}>
                    <div className="custom-form-input">
                        <Label for="username">Friend's Username</Label>
                        <Input
                            type="text"
                            required
                            name="username"
                            id="username"
                            value={username}
                            onChange={handleChange}
                        />
                    </div>
                    <div style={{ display: "flex", justifyContent: "space-between", marginTop: "20px" }}>
                        <Button
                            size="lg"
                            className="negative-button"
                            onClick={() => navigate("/friends")}
                        >
                            Cancel
                        </Button>
                        <Button
                            size="lg"
                            className="positive-button"
                            type="submit"
                            color='#b1d12d'
                        >
                            Send
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    );
}
