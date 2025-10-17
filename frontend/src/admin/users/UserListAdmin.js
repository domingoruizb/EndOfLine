import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [users, setUsers] = useFetchState(
    [],
    `/api/v1/users`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);
  const [confirmUserId, setConfirmUserId] = useState(null);

  const handleDeleteClick = (userId) => {
    setConfirmUserId(userId); 
  };

  const confirmDelete = () => {
    deleteFromList(
      `/api/v1/users/${confirmUserId}`,
      confirmUserId,
      [users, setUsers],
      [alerts, setAlerts],
      setMessage,
      setVisible
    );
    setConfirmUserId(null);
  };

  const cancelDelete = () => {
    setConfirmUserId(null);
  };


  const userList = users.map((user) => {
    return (
      <tr key={user.id}>
        <td>{user.username}</td>
        <td>{user.authority.authority}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + user.id}
              tag={Link}
              to={"/users/" + user.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + user.id}
              onClick={() =>
                handleDeleteClick(user.id)
              }
            > 
              Delete
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">Users</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <Button color="success" tag={Link} to="/users/new">
        Add User
      </Button>
      <div>
        <Table aria-label="users" className="mt-4">
          <thead>
            <tr>
              <th>Username</th>
              <th>Authority</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>{userList}</tbody>
        </Table>
      </div>
      {confirmUserId !== null && (
        <div className="auth-page-container">
          <div className="auth-form-container">
            <h2 className="text-center text-md">
              Are you sure you want to delete this user?
            </h2>
            <div className="options-row">
              <button className="auth-button" onClick={cancelDelete}>
                No
              </button>
              <button className="auth-button" onClick={confirmDelete}>
                Yes
              </button>
            </div>
          </div>
    </div>
      )}
    </div>
  );
}
