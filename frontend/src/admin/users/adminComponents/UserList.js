import { Button, ButtonGroup } from "reactstrap";
import { Link } from 'react-router-dom';

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
                    <Button
                        size="sm"
                        color="primary"
                        aria-label={"edit-" + user.id}
                        tag={Link}
                        to={"/users/" + user.id}
                        className="user-edit-button"
                    >
                        Edit
                    </Button>
                    <Button
                        size="sm"
                        color="danger"
                        aria-label={"delete-" + user.id}
                        onClick={() => handleDeleteClick(user.id)}
                        className="user-delete-button"
                    >
                        Delete
                    </Button>
                </ButtonGroup>
            </td>
        </tr>
    ));
}