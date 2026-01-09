import { useCallback } from 'react';
import tokenService from '../../services/token.service';

export function useProfileApi({ setMessage, setVisible, setPlayer, user, player, navigate, alerts, setAlerts }) {
  const handleSaveChanges = useCallback(async ({
    name, surname, username, avatar, email, birthdate, avatarError, nameError, surnameError, usernameError, emailError, birthdateError, jwt
  }) => {
    if (avatarError || nameError || surnameError || usernameError || emailError || birthdateError) {
      setMessage('Invalid data');
      setVisible(true);
      return;
    }
    try {
      const response = await fetch(`/api/v1/users/myself`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
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
          authority: player.authority,
        }),
      });
      //si cambia el username, se desloguea para que no haya conflictos
        if (response.ok) {
          if (username !== user.username) {
            tokenService.removeUser();
            window.location.href = '/login';
          } else {
            const updatedUser = { ...user, username };
            if (user && user.token) updatedUser.token = user.token;
            tokenService.setUser(updatedUser);
            window.location.href = '/myprofile';
          }
      } else {
        const data = await response.json();
        setMessage(data.message || 'Error updating player');
        setVisible(true);
      }
    } catch (error) {
      setMessage('Error updating player');
      setVisible(true);
    }
  }, [setMessage, setVisible, user, player]);

  const confirmDelete = useCallback(async (deleteMyself, jwt) => {
    const success = await deleteMyself(
      `/api/v1/users/myself`,
      user.id,
      [alerts, setAlerts],
      setMessage,
      setVisible
    );
    if (success) {
      navigate('/', { state: { message: 'Your account has been successfully deleted' } });
    }
  }, [alerts, setAlerts, setMessage, setVisible, user, navigate]);

  return { handleSaveChanges, confirmDelete };
}
