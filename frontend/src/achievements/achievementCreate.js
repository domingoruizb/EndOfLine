
import { useState } from "react";
import tokenService from "../services/token.service";
import { useNavigate } from "react-router-dom";
import getErrorModal from "../util/getErrorModal";
import FormGenerator from "../components/formGenerator/formGenerator";
import achievementFormInputs from "./achievementComponents/achievementFormInputs";
import "../static/css/admin/adminPage.css";
import LinkClickButton from "../components/LinkClickButton";

export default function AchievementCreate() {
  const jwt = tokenService.getLocalAccessToken();
  const emptyAchievement = {
    name: "",
    description: "",
    badgeImage: "",
    threshold: 1,
    category: "GAMES_PLAYED",
    actualDescription: ""
  };

  const [achievement, setAchievement] = useState(emptyAchievement);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const navigate = useNavigate();
  const modal = getErrorModal(setVisible, visible, message);


  function handleSubmit({ values, errors }) {
    if (errors && Object.values(errors).some(Boolean)) {
      setMessage('Invalid data');
      setVisible(true);
      return;
    }
    fetch("/api/v1/achievements", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ ...achievement, ...values }),
    })
      .then((response) => response.text())
      .then((data) => {
        if (data === "") navigate("/achievements");
        else {
          let json = JSON.parse(data);
          if (json.message) {
            setMessage(json.message);
            setVisible(true);
          } else navigate("/achievements");
        }
      })
      .catch((message) => alert(message));
  }



  return (
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">Add Achievement</h1>
        {modal}
        <div className="form-container">
          <FormGenerator
            inputs={achievementFormInputs(achievement)}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            buttonText="SAVE"
            buttonClassName="button"
            childrenPosition={-1}
          >
            <LinkClickButton
              text='CANCEL'
              link='/achievements'
              className='danger'
            />
          </FormGenerator>
        </div>
      </div>
    </div>
  );
}
