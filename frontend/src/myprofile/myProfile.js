
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container } from "reactstrap";
import DeleteProfileModal from "./myProfileComponents/DeleteProfileModal";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import '../static/css/myProfile/myProfile.css';
import '../static/css/home/home.css';
import getErrorModal from "../util/getErrorModal";
import deleteMyself from "../util/deleteMyself";
import useFetchState from "../util/useFetchState";
import ProfileInput from "./myProfileComponents/ProfileInput";
import AvatarPreview from "./myProfileComponents/AvatarPreview";
import useProfileForm from "../hooks/useProfileForm";
import { useProfileApi } from "../hooks/useProfileApi";


const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();


export default function MyProfile() {
  const [player, setPlayer] = useFetchState(null, `/api/v1/users/myself`, jwt);
  const [message, setMessage] = useState("");
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [deleteProfile, setDeleteProfile] = useState(false);
  const navigate = useNavigate();

  const form = useProfileForm({
    name: player?.name,
    surname: player?.surname,
    username: player?.username,
    avatar: player?.avatar,
    email: player?.email,
    birthdate: player?.birthdate,
  });

  const { handleSaveChanges, confirmDelete } = useProfileApi({
    setMessage,
    setVisible,
    setPlayer,
    user,
    player,
    navigate,
    alerts,
    setAlerts,
  });

  useEffect(() => {
    if (player) {
      form.setName(player.name || "");
      form.setSurname(player.surname || "");
      form.setUsername(player.username || "");
      form.setAvatar(player.avatar || "");
      form.setEmail(player.email || "");
      form.setBirthdate(player.birthdate || "");
    }
  }, [player, form]);

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="login-page-container">
      <Container className="auth-page-container myProfile-container">
        <h1 className="text-center myProfile-title">My Profile</h1>
        {modal}
        <div className="auth-form-container">
          <div>
            <ProfileInput
              id="username"
              label="Username"
              value={form.username}
              onChange={form.handleUsernameChange}
              error={form.usernameError}
            />
            <ProfileInput
              id="name"
              label="Name"
              value={form.name}
              onChange={form.handleNameChange}
              error={form.nameError}
            />
            <ProfileInput
              id="surname"
              label="Surname"
              value={form.surname}
              onChange={form.handleSurnameChange}
              error={form.surnameError}
            />
            <ProfileInput
              id="email"
              label="Email"
              value={form.email}
              onChange={form.handleEmailChange}
              error={form.emailError}
            />
            <ProfileInput
              id="birthdate"
              label="Birthdate"
              value={form.birthdate}
              onChange={form.handleBirthdateChange}
              error={form.birthdateError}
              type="date"
            />
            <ProfileInput
              id="avatar"
              label="Avatar"
              value={form.avatar}
              onChange={form.handleAvatarChange}
              error={form.avatarError}
              style={{ marginBottom: "20px" }}
            />
            <AvatarPreview avatar={form.avatar} error={form.avatarError} />
            <div className="custom-button-row">
              <button color="danger" type="button" onClick={() => setDeleteProfile(true)} className="auth-button danger">
                Delete profile
              </button>
              <button className="auth-button" type="button" onClick={() => handleSaveChanges({
                ...form,
                jwt,
              })}>
                Save changes
              </button>
            </div>
          </div>
        </div>
        {deleteProfile && (
          <DeleteProfileModal
            isOpen={deleteProfile}
            toggle={() => setDeleteProfile(false)}
            onConfirm={() => confirmDelete(deleteMyself, jwt)}
          />
        )}
      </Container>
    </div>
  );
}