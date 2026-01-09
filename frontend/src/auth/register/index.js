
import { useState } from "react";
import { Container } from "reactstrap";
import getErrorModal from "../../util/getErrorModal";
import "../../static/css/auth/login.css";
import "../../static/css/auth/authPage.css";
import "../../static/css/myProfile/myProfile.css";
import FormGenerator from "../../components/formGenerator/formGenerator";
import { registerFormPlayer } from "./form/registerFormInputs";


export default function Register() {
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [avatar, setAvatar] = useState("https://cdn3.iconfinder.com/data/icons/avatar-165/536/NORMAL_HAIR-512.png");
  const modal = getErrorModal(setVisible, visible, message);
  const handleAvatarChange = (e) => {
    setAvatar(e.target.value || "https://cdn3.iconfinder.com/data/icons/avatar-165/536/NORMAL_HAIR-512.png");
  };

  const registerInputs = registerFormPlayer.map(input =>
    input.name === "avatar"
      ? { ...input, onChange: handleAvatarChange, defaultValue: avatar }
      : input
  );

  const handleRegister = async ({ values }) => {
    try {
      const response = await fetch(`/api/v1/auth/signup`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          ...values,
          authority: "PLAYER",
        }),
      });
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
    <div className={"login-page-container"}>
      <Container className="auth-page-container"
        style={{
          padding: "0 1rem",
          maxWidth: 900,
          background: "none",
          borderRadius: "1rem",
          boxShadow: "none",
        }}>
        <h1 className="text-center" style={{
          fontWeight: 800,
          letterSpacing: "2px",
          color: "#FE5B02",
          textShadow: "0 2px 8px #000"
        }}>
          Register
        </h1>
        {modal}
        <div className="auth-form-container">
          <div style={{ textAlign: "center", marginTop: "10px", marginBottom: "30px" }}>
            <img
              src={avatar}
              alt="avatar"
              style={{ width: "100px", height: "100px", borderRadius: "50%" }}
            />
          </div>
          <FormGenerator
            inputs={registerInputs}
            onSubmit={handleRegister}
            numberOfColumns={1}
            buttonText="Register"
            buttonClassName="auth-button orange-button"
            childrenPosition={-1}
          />
        </div>
      </Container>
    </div>
  );
}

