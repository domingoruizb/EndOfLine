import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DeleteProfileModal from './myProfileComponents/DeleteProfileModal';
import tokenService from '../services/token.service';
import getErrorModal from '../util/getErrorModal';
import deleteMyself from '../util/deleteMyself';
import useFetchState from '../util/useFetchState';
import FormGenerator from '../components/formGenerator/formGenerator';
import profileFormInputs from './myProfileComponents/profileFormInputs';
import AvatarPreview from './myProfileComponents/AvatarPreview';
import useProfileForm from './myProfileHooks/useProfileForm';
import { useProfileApi } from './myProfileHooks/useProfileApi';
import LinkClickButton from '../components/LinkClickButton'


const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();


export default function MyProfile() {
  const [player, setPlayer] = useFetchState(null, `/api/v1/users/myself`, jwt);
  const [message, setMessage] = useState('');
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
      form.setName(player.name || '');
      form.setSurname(player.surname || '');
      form.setUsername(player.username || '');
      form.setAvatar(player.avatar || '');
      form.setEmail(player.email || '');
      form.setBirthdate(player.birthdate || '');
    }
  }, [player, form]);

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className='page-container'>
      <div className='info-container'>
        {modal}
        <h1 className='info-title'>
          My Profile
        </h1>
        <AvatarPreview avatar={form.avatar} error={form.avatarError} />
        <div className='form-container'>
          <FormGenerator
            inputs={profileFormInputs(form)}
            onSubmit={({ values }) => handleSaveChanges({ ...form, ...values, jwt })}
            numberOfColumns={1}
            buttonText='SAVE'
            buttonClassName='button'
            childrenPosition={-1}
          >
            <LinkClickButton
              text='DELETE'
              onClick={() => setDeleteProfile(true)}
              className='danger'
            />
          </FormGenerator>
        </div>
        <DeleteProfileModal
          isOpen={deleteProfile}
          toggle={() => setDeleteProfile(false)}
          onConfirm={() => confirmDelete(deleteMyself, jwt)}
        />
      </div>
    </div>
  );
}