
import { useState } from "react";
import getErrorModal from "../../util/getErrorModal";
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
    <div className="page-container">
      <div className="info-container no-bg">
        <h1 className="info-title">
          Register
        </h1>
        {modal}
        <div className="form-container">
          <div style={{ textAlign: "center", marginTop: "10px", marginBottom: "30px" }}>
            <img
              src={avatar}
              alt="avatar"
              style={{ width: "150px", height: "150px", borderRadius: "50%" }}
            />
          </div>
          <FormGenerator
            inputs={registerInputs}
            onSubmit={handleRegister}
            numberOfColumns={1}
            buttonText="REGISTER"
            buttonClassName="button"
            childrenPosition={-1}
          />
        </div>
      </div>
    </div>
  );
}

