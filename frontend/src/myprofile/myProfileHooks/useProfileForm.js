import { useState, useCallback } from 'react';

export default function useProfileForm(initial) {
  const [name, setName] = useState(initial.name || '');
  const [nameError, setNameError] = useState('');
  const [surname, setSurname] = useState(initial.surname || '');
  const [surnameError, setSurnameError] = useState('');
  const [username, setUsername] = useState(initial.username || '');
  const [usernameError, setUsernameError] = useState('');
  const [avatar, setAvatar] = useState(initial.avatar || '');
  const [avatarError, setAvatarError] = useState('');
  const [email, setEmail] = useState(initial.email || '');
  const [emailError, setEmailError] = useState('');
  const [birthdate, setBirthdate] = useState(initial.birthdate || '');
  const [birthdateError, setBirthdateError] = useState('');

  // Validation handlers
  const handleNameChange = useCallback((event) => {
    const newName = event.target.value;
    setName(newName);
    if (newName.length < 3 || newName.length > 15) {
      setNameError('Name must be between 3 and 15 characters');
    } else {
      setNameError('');
    }
  }, []);

  const handleUsernameChange = useCallback((event) => {
    const newUsername = event.target.value;
    setUsername(newUsername);
    if (newUsername.length < 3 || newUsername.length > 15) {
      setUsernameError('Username must be between 3 and 15 characters');
    } else {
      setUsernameError('');
    }
  }, []);

  const handleSurnameChange = useCallback((event) => {
    const newSurname = event.target.value;
    setSurname(newSurname);
    if (newSurname.length < 3 || newSurname.length > 15) {
      setSurnameError('Surname must be between 3 and 15 characters');
    } else {
      setSurnameError('');
    }
  }, []);

  const handleEmailChange = useCallback((event) => {
    const newEmail = event.target.value;
    setEmail(newEmail);
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(newEmail)) {
      setEmailError('Email format is invalid (ej: user@dom.com)');
    } else {
      setEmailError('');
    }
  }, []);

  const handleBirthdateChange = useCallback((event) => {
    const newDate = event.target.value;
    setBirthdate(newDate);
    const today = new Date();
    const birthdate = new Date(newDate);
    if (!newDate) {
      setBirthdateError('Birthdate cannot be empty.');
    } else if (birthdate > today) {
      setBirthdateError('The birthdate cannot be in the future.');
    } else {
      setBirthdateError('');
    }
  }, []);

  const handleAvatarChange = useCallback((event) => {
    const newAvatar = event.target.value;
    setAvatar(newAvatar);
    const avatarPattern = /^https?:\/\/.*\.(jpg|png|jpeg)$/i;
    if (newAvatar.length > 0 && !avatarPattern.test(newAvatar)) {
      setAvatarError('Invalid avatar URL');
    } else {
      setAvatarError('');
    }
  }, []);

  return {
    name, setName, nameError, handleNameChange,
    surname, setSurname, surnameError, handleSurnameChange,
    username, setUsername, usernameError, handleUsernameChange,
    avatar, setAvatar, avatarError, handleAvatarChange,
    email, setEmail, emailError, handleEmailChange,
    birthdate, setBirthdate, birthdateError, handleBirthdateChange,
  };
}
