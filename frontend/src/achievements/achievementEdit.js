import { useState } from "react";
import tokenService from "../services/token.service";
import { Link, useNavigate } from "react-router-dom";
import { Form } from "reactstrap";
import AchievementFormInput from "./achievementComponents/AchievementFormInput";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import getIdFromUrl from "../util/getIdFromUrl";
import "../static/css/admin/adminPage.css";

export default function AchievementEdit() {
  const id = getIdFromUrl(2);
  const jwt = tokenService.getLocalAccessToken();
  const emptyAchievement = {
    id: id,
    name: "",
    description: "",
    badgeImage: "",
    threshold: 1,
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
  // message and visible are now declared above
  const navigate = useNavigate();
  const modal = getErrorModal(setVisible, visible, message);

  function handleSubmit(event) {
    event.preventDefault();
    fetch(`/api/v1/achievements/${id}`,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(achievement),
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

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setAchievement({ ...achievement, [name]: value });
  }

  return (
    <div className="user-list-page">
      <div className="admin-page-container user-list-container">
        <h1 className="user-list-title">Edit Achievement</h1>
        {modal}
        <div className="auth-form-container">
          <Form onSubmit={handleSubmit}>
            <AchievementFormInput
              label="Name"
              name="name"
              type="text"
              value={achievement.name || ""}
              onChange={handleChange}
              required
            />
            <AchievementFormInput
              label="Description"
              name="description"
              type="text"
              value={achievement.description || ""}
              onChange={handleChange}
              required
            />
            <AchievementFormInput
              label="Badge Image Url:"
              name="badgeImage"
              type="text"
              value={achievement.badgeImage || ""}
              onChange={handleChange}
              required
            />
            <AchievementFormInput
              label="Category"
              name="category"
              type="select"
              value={achievement.category || ""}
              onChange={handleChange}
              required
              options={["GAMES_PLAYED", "VICTORIES", "TOTAL_PLAY_TIME"]}
            />
            <AchievementFormInput
              label="Threshold value:"
              name="threshold"
              type="number"
              value={achievement.threshold || ""}
              onChange={handleChange}
              required
            />
            <div className="custom-button-row">
              <button className="user-add-button" type="submit">Save</button>
              <Link
                to={`/achievements`}
                className="user-add-button"
                style={{ textDecoration: "none", background: "#555", marginLeft: "1rem" }}
              >
                Cancel
              </Link>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
}
