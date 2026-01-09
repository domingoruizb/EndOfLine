
import { useNavigate } from "react-router-dom";
import FormGenerator from "../components/formGenerator/formGenerator";
import createFriendshipInputs from "./friendshipComponents/createFriendshipInputs";
import { useFetchResource } from '../util/useFetchResource';
import { showSuccessToast } from '../util/toasts';
import "../static/css/friendships/friendsList.css";

export default function FriendshipCreation() {
    let navigate = useNavigate();
    const { getData } = useFetchResource()

    const handleSubmit = async ({ values }) => {
        const { success } = await getData(`/api/v1/friendships`, 'POST', { receiver: values.username })

        if (success) {
            showSuccessToast(`Friendship request sent to ${values.username}!`)
            navigate('/friends')
        }
    }

    return (
        <div className="friend-list-page">
            <div className="friend-content-wrapper">
                <div className="create-friendship-container">
                    <h1 className="create-friendship-title">
                        Send Friendship Request
                    </h1>
                    <FormGenerator
                        inputs={createFriendshipInputs()}
                        onSubmit={handleSubmit}
                        numberOfColumns={1}
                        buttonText="Send"
                        buttonClassName="create-friendship-send-button"
                        childrenPosition={-1}
                        listenEnterKey={false}
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
