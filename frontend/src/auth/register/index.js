import { useState } from "react";
import "../../static/css/admin/adminPage.css";
import tokenService from "../../services/token.service";
import { Input, Label } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import "../../static/css/auth/login.css";

export default function Register() {
const jwt = tokenService.getLocalAccessToken();
  const [player, setPlayer] = useState({});
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
   const [name, setName] = useState("");
  const [nameError, setNameError] = useState("");
  const [surname, setSurname] = useState("");
  const [surnameError, setSurnameError] = useState("");
  const [username, setUsername] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [avatar, setAvatar] = useState("https://cdn3.iconfinder.com/data/icons/avatar-165/536/NORMAL_HAIR-512.png");
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");
  const [password, setPassword] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [avatarError, setAvatarError] = useState("");
  const [birthdate, setBirthdate] = useState("");
  const [birthdateError, setBirthdateError] = useState("");

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

  const handlePasswordChange = (event) => {
    const newPassword = event.target.value;
    setPassword(newPassword);
    if (newPassword.length < 3 || newPassword.length > 15) {
      setPasswordError("Password must be between 5 and 15 characters");
    } else {
      setPasswordError("");
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
    if (avatarError || nameError || surnameError || usernameError || emailError || birthdateError || passwordError) {
      setMessage("Invalid data");
      setVisible(true);
      return;
    }

    try {
      console.log({
          name,
          surname,
          username,
          avatar,
          password,
          email,
          birthdate,
          authority: { id: 2, authority: "PLAYER" },
      })
      const response = await fetch(`/api/v1/auth/signup`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          name,
          surname,
          username,
          avatar,
          password,
          email,
          birthdate,
          authority: "PLAYER",
        }),
      }
    );
      console.log(response);
      if (response.ok) {
        window.location.href = "/login";
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

  return (
    <div className="login-page-container">
<div className="auth-page-container">
      {<h2>{"Register"}</h2>}
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
              onChange={handleEmailChange}
              className="custom-input"
            />
          </div> 
          <div className="custom-form-input">
            <Label for="password" className="custom-form-input-label">
              Password
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              onChange={handlePasswordChange}
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
              onChange={handleBirthdateChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="avatar" className="custom-form-input-label">
              Avatar
            </Label>
            <Input style={{ marginBottom: "20px" }} type="text" value={avatar} onChange={handleAvatarChange}  className="custom-input" />
            {!avatarError && avatar && (
            <div style={{ textAlign: "center", marginTop: "10px", marginBottom: "10px" }}>
              <img
                src={avatar}
                alt="avatar"
                style={{ width: "100px", height: "100px", borderRadius: "50%" }}
              />
            </div>
            )}
            <div className="custom-button-row" style={{marginBottom: "20px" }}>
              <button className="auth-button" type="button" onClick={handleSaveChanges} >Register</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  );
}

