
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
import FormGenerator from "../components/formGenerator/formGenerator";
import profileFormInputs from "./myProfileComponents/profileFormInputs";
import AvatarPreview from "./myProfileComponents/AvatarPreview";
import useProfileForm from "./myProfileHooks/useProfileForm";
import { useProfileApi } from "./myProfileHooks/useProfileApi";


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
          <AvatarPreview avatar={form.avatar} error={form.avatarError} />
          <FormGenerator
            inputs={profileFormInputs(form)}
            onSubmit={({ values }) => handleSaveChanges({ ...form, ...values, jwt })}
            numberOfColumns={1}
            buttonText="Save changes"
            buttonClassName="auth-button"
            childrenPosition={-1}
          />
          <div style={{ display: 'flex', justifyContent: 'center', gap: '24px', marginTop: '32px' }}>
            <button
              color="danger"
              type="button"
              onClick={() => setDeleteProfile(true)}
              className="auth-button danger"
              style={{ minWidth: '180px', marginBottom: '16px' }}
            >
              Delete profile
            </button>
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