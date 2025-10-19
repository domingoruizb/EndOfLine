import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Modal, ModalHeader, ModalBody, Button, Input, Label, ModalFooter } from "reactstrap";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import deleteMyself from "../util/deleteMyself";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();

export default function MyProfile() {
  const [player, setPlayer] = useFetchState(null, `/api/v1/users/myself`, jwt);
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
   const [name, setName] = useState("");
  const [nameError, setNameError] = useState("");
  const [surname, setSurname] = useState("");
  const [surnameError, setSurnameError] = useState("");
  const [username, setUsername] = useState("");
    const [usernameError, setUsernameError] = useState("");
  const [avatar, setAvatar] = useState("");
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");
  const [avatarError, setAvatarError] = useState("");
  const [birthdate, setBirthdate] = useState("");
  const [birthdateError, setBirthdateError] = useState("");

  const [deleteProfile, setDeleteProfile] = useState(false);
  const navigate = useNavigate();
 
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/v1/users/myself`, {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        });
        const data = await response.json();
        setPlayer(data);
        setName(data.name);
        setSurname(data.surname);
        setUsername(data.username);
        setAvatar(data.avatar);
        setEmail(data.email);
        setBirthdate(data.birthdate);
        
      } catch (error) {
        setMessage("Error fetching player data");
        setVisible(true);
      }
    };
    fetchData();
  }, [jwt, setPlayer]);

  const modal = getErrorModal(setVisible, visible, message);

  const formatDate = (date) => {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    return new Date(date).toLocaleDateString(undefined, options);
  };

  const handleNameChange = (event) => {
    const newName = event.target.value;
    setName(newName);
    if (newName.length < 3 || newName.length > 15) {
      setNameError("Name must be between 3 and 15 characters");
    } else {
      setNameError("");
    }
  };

  const handleUsernameChange = (event) => {
    const newUsername = event.target.value;
    setUsername(newUsername);
    if (newUsername.length < 3 || newUsername.length > 15) {
      setUsernameError("Username must be between 3 and 15 characters");
    } else {
      setUsernameError("");
    }
  };

  const handleSurnameChange = (event) => {
    const newSurname = event.target.value;
    setSurname(newSurname);
    if (newSurname.length < 3 || newSurname.length > 15) {
      setSurnameError("Surname must be between 3 and 15 characters");
    }
    else {
      setSurnameError("");
    }
  };

  const handleEmailChange = (event) => {
    const newEmail = event.target.value;
    setEmail(newEmail);
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(newEmail)) {
        setEmailError("Email format is invalid (ej: user@dom.com)");
    } else {
        setEmailError("");
    }
  };

  const handleBirthdateChange = (event) => {
    const newDate = event.target.value;
    setBirthdate(newDate);
    const today = new Date();

    const birthdate = new Date(newDate);

    if (!newDate) {
       setBirthdateError("Birthdate cannot be empty.");
    } 
    else if (birthdate > today) {
        setBirthdateError("The birthdate cannot be in the future.");
    }
    else {
        setBirthdateError("");
    }
};

  const handleAvatarChange = (event) => {
    const newAvatar = event.target.value;
    setAvatar(newAvatar);    
    const avatarPattern = /^https?:\/\/.*\.(jpg|png|jpeg)$/i;
    if(newAvatar.length > 0 && !avatarPattern.test(newAvatar)) {
      setAvatarError("Invalid avatar URL");
    } else {
      setAvatarError("");
    }
  };

  const handleSaveChanges = async () => {
    if (avatarError || nameError || surnameError || usernameError || emailError || birthdateError) {
      setMessage("Invalid data");
      setVisible(true);
      return;
    }
    console.log(avatar);
    try {
      const response = await fetch(`/api/v1/users/myself`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
        body: JSON.stringify({
          id: user.id,
          name,
          surname,
          username,
          avatar,
          password: player.password,
          email,
          birthdate,
          authority: player.authority
        }),
      }
    );

      if (response.ok) {
        window.location.href = "/myprofile";
      } else {
        const data = await response.json();
        setMessage(data.message || "Error updating player");
        setVisible(true);
      }
    } catch (error) {
      setMessage("Error updating player");
      setVisible(true);
    }
  };

  const handleDeleteClick = () => {
    setDeleteProfile(true); 
  };

  const cancelDelete = () => {
    setDeleteProfile(false);
  };

  const confirmDelete = async () => {
      const success = await deleteMyself(
        `/api/v1/users/myself`,
        user.id,
        [alerts, setAlerts],
        setMessage,
        setVisible
      );
      setDeleteProfile(false);

      if (success) {
         navigate("/", { state: { message: "Your account has been successfully deleted" } });
      }
    };

  return (
    <div style={{
          backgroundColor: "black",
          color: "white",
          minHeight: "100vh",
          padding: "2rem 0",
          fontFamily: "Inter, Arial, sans-serif",
        }}>
    <div className="auth-page-container">
      {<h2>{"My Profile"}</h2>}
      {modal}
      <div className="auth-form-container">
        <div>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Username
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={username || ""}
              onChange={handleUsernameChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="name" className="custom-form-input-label">
              Name
            </Label>
            <Input
              type="text"
              required
              name="name"
              id="name"
              value={name || ""}
              onChange={handleNameChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="surname" className="custom-form-input-label">
              Surname
            </Label>
            <Input
              type="text"
              required
              name="surname"
              id="surname"
              value={surname || ""}
              onChange={handleSurnameChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="email" className="custom-form-input-label">
              Email
            </Label>
            <Input
              type="text"
              required
              name="email"
              id="email"
              value={email || ""}
              onChange={handleEmailChange}
              className="custom-input"
            />
          </div>     
          <div className="custom-form-input">
            <Label for="birthdate" className="custom-form-input-label">
              Birthdate
            </Label>
            <Input
              type="date"
              required
              name="birthdate"
              id="birthdate"
              value={birthdate || ""}
              onChange={handleBirthdateChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="avatar" className="custom-form-input-label">
              Avatar
            </Label>
            <Input style={{ marginBottom: "20px" }} type="text" value={avatar || ""} onChange={handleAvatarChange}  className="custom-input" />
            {!avatarError && avatar && (
            <div style={{ textAlign: "center", marginTop: "10px" }}>
              <img
                src={avatar}
                alt="avatar"
                style={{ width: "100px", height: "100px", borderRadius: "50%" }}
              />
            </div>
            )}
          </div>
          <div className="custom-button-row">
            <button color="danger" type="button" onClick={handleDeleteClick} className="auth-button danger">
              Delete profile
            </button>
            <button className="auth-button" type="button" onClick={handleSaveChanges} >Save changes</button>
          </div>
        </div>
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
    </div>
    
  );
  }