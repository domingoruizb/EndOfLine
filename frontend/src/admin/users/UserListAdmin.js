import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, ButtonGroup, Table, Container } from "reactstrap";
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
              style={{
                background: "#B1D12D",
                marginRight: "0.5rem",
                color: "#fff",
                fontWeight: 600,
                border: "none"
              }}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + user.id}
              onClick={() => handleDeleteClick(user.id)}
              style={{
                background: "#FE5B02",
                
                color: "#fff",
                fontWeight: 600,
                border: "none"
              }}
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
    <div
      style={{
        backgroundColor: "#000",
        color: "#fff",
        minHeight: "100vh",
        padding: "2rem 0",
        fontFamily: "Inter, Arial, sans-serif",
      }}
    >
      <Container
        className="admin-page-container"
        style={{
          padding: "0 1rem",
          maxWidth: 900,
          background: "none",
          borderRadius: "1rem",
          boxShadow: "none",
        }}
      >
        <h1 className="text-center" style={{
          fontWeight: 800,
          letterSpacing: "2px",
          marginBottom: "2rem",
          color: "#FE5B02",
          textShadow: "0 2px 8px #000"
        }}>
          Users
        </h1>
        {alerts.map((a) => a.alert)}
        {modal}
        <Button
          color="success"
          tag={Link}
          to="/users/new"
          style={{
            background: "linear-gradient(90deg, #FE5B02 60%, #B1D12D 100%)",
            color: "#fff",
            fontWeight: 700,
            border: "none",
            marginBottom: "1.5rem",
            borderRadius: "0.4em"
          }}
        >
          Add User
        </Button>
        <div>
          <Table
            aria-label="users"
            className="mt-4"
            style={{
              background: "#181818",
              borderRadius: "0.6rem",
              overflow: "hidden",
              boxShadow: "0 2px 16px #222",
              color: "#fff"
            }}
            dark
            hover
          >
            <thead>
              <tr style={{ background: "linear-gradient(90deg, #FE5B02 40%, #111 60%, #B1D12D 100%)"}}>
                <th style={{
                  color: "#FE5B02",
                  fontWeight: 700,
                  padding: "0.85em"
                }}>Username</th>
                <th style={{
                  color: "#B1D12D",
                  fontWeight: 700,
                  padding: "0.85em"
                }}>Authority</th>
                <th style={{
                  color: "#fff",
                  fontWeight: 700,
                  padding: "0.85em"
                }}>Actions</th>
              </tr>
            </thead>
            <tbody>{userList}</tbody>
          </Table>
        </div>
        {confirmUserId !== null && (
          <Modal isOpen={confirmUserId !== null} toggle={cancelDelete}>
            <ModalHeader
              toggle={cancelDelete}
              style={{
                color: "#FE5B02",
                background: "#111"
              }}
            >
              Confirm Deletion
            </ModalHeader>
            <ModalBody style={{ background: "#111", color: "#fff" }}>
              Are you sure you want to delete this user?
            </ModalBody>
            <ModalFooter style={{ background: "#111" }}>
              <Button color="secondary" onClick={cancelDelete} style={{
                background: "#323232", color: "#fff", border: "none"
              }}>
                Cancel
              </Button>
              <Button color="danger" onClick={confirmDelete} style={{
                background: "linear-gradient(90deg, #FE5B02 40%, #B1D12D 80%)",
                color: "#fff", border: "none"
              }}>
                Delete
              </Button>
            </ModalFooter>
          </Modal>
        )}
      </Container>
    </div>
  );
}
