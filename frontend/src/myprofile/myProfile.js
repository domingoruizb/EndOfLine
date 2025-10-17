import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Form, Modal, ModalHeader, ModalBody, Button, Input, Label, ModalFooter } from "reactstrap";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import getIdFromUrl from "../util/getIdFromUrl";
import deleteMyself from "../util/deleteMyself";
import useFetchData from "../util/useFetchData";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function MyProfile() {
  const emptyItem = {
    id: null,
    username: "",
    name: "",
    surname: "",
    password: "",
    birthdate: "", 
    email: "",
    authority: null,
  };
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [deleteProfile, setDeleteProfile] = useState(false);
  const navigate = useNavigate();
  const [user, setUser] = useFetchState(
    emptyItem,
    `/api/v1/users/myself`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  // const auths = useFetchData(`/api/v1/users/authorities`, jwt);

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();

    fetch("/api/v1/users/myself", {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else window.location.href = "/myprofile";
      })
      .catch((message) => alert(message));
  }

  const handleDeleteClick = () => {
    setDeleteProfile(true); 
  };

  const cancelDelete = () => {
    setDeleteProfile(false);
  };

  const confirmDelete = () => {
      const success = deleteMyself(
        `/api/v1/users/${currentUser.id}`,
        currentUser.id,
        [alerts, setAlerts],
        setMessage,
        setVisible
      );
      setDeleteProfile(false);

      if (success) {
        navigate("/");
      }
    };

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="auth-page-container">
      {<h2>{"My Profile"}</h2>}
      {modal}
      <div className="auth-form-container">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Username
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Name
            </Label>
            <Input
              type="text"
              required
              name="name"
              id="name"
              value={user.name || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Surname
            </Label>
            <Input
              type="text"
              required
              name="surname"
              id="surname"
              value={user.surname || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Email
            </Label>
            <Input
              type="text"
              required
              name="email"
              id="email"
              value={user.email || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Password
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>          
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              BirthDate
            </Label>
            <Input
              type="date"
              required
              name="birthdate"
              id="birthdate"
              value={user.birthdate || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-button-row">
            <button color="danger" type="button" onClick={handleDeleteClick} className="auth-button danger">
              Delete profile
            </button>
            <button className="auth-button">Save changes</button>
          </div>
        </Form>
      </div>
      {deleteProfile && (
        <Modal isOpen={deleteProfile} toggle={cancelDelete}>
          <ModalHeader toggle={cancelDelete}>Confirm Deletion</ModalHeader>
          <ModalBody>
            Are you sure you want to delete your profile?
          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={cancelDelete}>
              Cancel
            </Button>
            <Button color="danger" onClick={confirmDelete}>
              Delete
            </Button>
          </ModalFooter>
        </Modal>
      )}
    </div>
  );
}
