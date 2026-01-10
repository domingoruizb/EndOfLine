import { Button, ButtonGroup } from "reactstrap";
import { Link } from 'react-router-dom';
import LinkClickButton from "../../../components/LinkClickButton";

export default function UserList ({
    users,
    handleDeleteClick
}) {
    return users.map((user) => (
        <tr key={user.id}>
            <td className="text-center">{user.username}</td>
            <td className="text-center">{user.authority.type}</td>
            <td className="text-center">
                <ButtonGroup>
                    <LinkClickButton
                        text='EDIT'
                        link={'/users/' + user.id}
                        className='sm'
                        aria-label={"edit-" + user.id}
                    />
                    <LinkClickButton
                        text='DELETE'
                        onClick={() => handleDeleteClick(user.id)}
                        className='sm danger'
                        aria-label={"delete-" + user.id}
                    />
                </ButtonGroup>
            </td>
        </tr>
    ));
}