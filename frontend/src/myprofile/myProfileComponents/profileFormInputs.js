// Configuración de inputs para el formulario de perfil de usuario
const profileFormInputs = (form) => [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: form.username,
    isRequired: true,
    error: form.usernameError,
    onChange: form.handleUsernameChange,
  },
  {
    tag: "Name",
    name: "name",
    type: "text",
    defaultValue: form.name,
    isRequired: true,
    error: form.nameError,
    onChange: form.handleNameChange,
  },
  {
    tag: "Surname",
    name: "surname",
    type: "text",
    defaultValue: form.surname,
    isRequired: true,
    error: form.surnameError,
    onChange: form.handleSurnameChange,
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: form.email,
    isRequired: true,
    error: form.emailError,
    onChange: form.handleEmailChange,
  },
  {
    tag: "Birthdate",
    name: "birthdate",
    type: "date",
    defaultValue: form.birthdate,
    isRequired: true,
    error: form.birthdateError,
    onChange: form.handleBirthdateChange,
  },
  {
    tag: "Avatar",
    name: "avatar",
    type: "text",
    defaultValue: form.avatar,
    isRequired: false,
    error: form.avatarError,
    onChange: form.handleAvatarChange,
  },
];

export default profileFormInputs;
