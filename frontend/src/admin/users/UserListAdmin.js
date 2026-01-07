import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, ButtonGroup } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/userListAdmin.css";
import "../../static/css/admin/userListAdmin.css";
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
  const [page, setPage] = useState(1);
  const itemsPerPage = 10;

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

  const totalPages = Math.ceil((users?.length || 0) / itemsPerPage) || 1;
  const start = (page - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const visibleUsers = users.slice(start, end);

  const userList = visibleUsers.map((user) => {
    return (
      <tr key={user.id}>
        <td className="text-center">{user.username}</td>
        <td className="text-center">{user.authority.authority}</td>
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
    );
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="user-list-page">
      <div className="user-list-container admin-page-container">
        <h1 className="text-center user-list-title">Users</h1>
        {alerts.map((a) => a.alert)}
        {modal}
        <div className="text-center user-list-actions mb-4">
          <Button
            color="success"
            tag={Link}
            to="/users/new"
            className="user-add-button"
          >
            Add User
          </Button>
        </div>
        {users.length > 0 ? (
          <>
            <table aria-label="users" className="mt-4 text-white user-table">
              <thead>
                <tr>
                  <th className="text-center">Username</th>
                  <th className="text-center">Authority</th>
                  <th className="text-center">Actions</th>
                </tr>
              </thead>
              <tbody>{userList}</tbody>
            </table>
            {totalPages > 1 && (
              <div className="text-center mt-4 pagination-container">
                <button
                  onClick={() => setPage(prev => Math.max(prev - 1, 1))}
                  disabled={page === 1}
                  className="pagination-button"
                >
                  Previous
                </button>
                <span className="pagination-info">
                  Page {page} of {totalPages}
                </span>
                <button
                  onClick={() => setPage(prev => Math.min(prev + 1, totalPages))}
                  disabled={page === totalPages}
                  className="pagination-button"
                >
                  Next
                </button>
              </div>
            )}
          </>
        ) : (
          <p className="text-center text-white mt-4">No users found.</p>
        )}
        {confirmUserId !== null && (
          <Modal isOpen={confirmUserId !== null} toggle={cancelDelete}>
            <ModalHeader toggle={cancelDelete} className="user-modal-header">
              Confirm Deletion
            </ModalHeader>
            <ModalBody className="user-modal-body">
              Are you sure you want to delete this user?
            </ModalBody>
            <ModalFooter className="user-modal-footer">
              <Button color="secondary" onClick={cancelDelete} className="user-modal-cancel">
                Cancel
              </Button>
              <Button color="danger" onClick={confirmDelete} className="user-modal-delete">
                Delete
              </Button>
            </ModalFooter>
          </Modal>
        )}
      </div>
    </div>
  );
}
