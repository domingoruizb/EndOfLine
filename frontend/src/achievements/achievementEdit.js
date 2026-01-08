
import { useState, useRef } from "react";
import tokenService from "../services/token.service";
import { Link, useNavigate } from "react-router-dom";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import getIdFromUrl from "../util/getIdFromUrl";
import FormGenerator from "../components/formGenerator/formGenerator";
import achievementFormInputs from "./achievementComponents/achievementFormInputs";
import "../static/css/admin/adminPage.css";

export default function AchievementEdit() {
  const id = getIdFromUrl(2);
  const jwt = tokenService.getLocalAccessToken();
  const emptyAchievement = {
    id: id,
    name: "",
    description: "",
    badgeImage: "",
    threshold: "",
    category: "GAMES_PLAYED",
    actualDescription: ""
  };

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [achievement, setAchievement] = useFetchState(
    emptyAchievement,
    `/api/v1/achievements/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const navigate = useNavigate();
  const modal = getErrorModal(setVisible, visible, message);


  function handleSubmit({ values }) {
    fetch(`/api/v1/achievements/${id}`,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...achievement, ...values }),
      }
    )
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

  const inputs = achievementFormInputs(achievement);



  return (
    <div className="user-list-page">
      <div className="admin-page-container user-list-container">
        <h1 className="user-list-title">Edit Achievement</h1>
        {modal}
        <div className="auth-form-container">
          <FormGenerator
            inputs={inputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            buttonText="Save"
            buttonClassName="user-add-button"
            childrenPosition={-1}
          >
            <Link
              to="/achievements"
              className="user-add-button"
              style={{ textDecoration: "none", background: "#555", marginBottom: '85px' }}
            >
              Cancel
            </Link>
          </FormGenerator>
        </div>
      </div>
    </div>
  );
}
