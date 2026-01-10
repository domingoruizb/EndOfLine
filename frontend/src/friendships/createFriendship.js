
import { useNavigate } from "react-router-dom";
import FormGenerator from "../components/formGenerator/formGenerator";
import createFriendshipInputs from "./friendshipComponents/createFriendshipInputs";
import { useFetchResource } from '../util/useFetchResource';
import { showSuccessToast } from '../util/toasts';
import LinkClickButton from "../components/LinkClickButton";
import "../static/css/friendships/friendsList.css";

export default function FriendshipCreation() {
    let navigate = useNavigate();
    const { getData } = useFetchResource()

    const handleSubmit = async ({ values }) => {
        const { status } = await getData(`/api/v1/friendships`, 'POST', { receiver: values.username })

        if (status === 'success') {
            showSuccessToast(`Friendship request sent to ${values.username}!`)
            navigate('/friends')
        }
    }

    return (
        <div className="page-container">
            <div className="info-container">
                <h1 className="info-title">
                    Send Friendship Request
                </h1>
                <FormGenerator
                    inputs={createFriendshipInputs()}
                    onSubmit={handleSubmit}
                    numberOfColumns={1}
                    buttonText="SEND"
                    buttonClassName="button"
                    childrenPosition={-1}
                    listenEnterKey={false}
                >
                    <LinkClickButton
                        to="/friends"
                        text="CANCEL"
                        className="danger"
                    />
                </FormGenerator>
            </div>
        </div>
    );
}
