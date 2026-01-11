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
    validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 20,  
        message: "Username must be between 3 and 20 characters."
      }
    ],
  },
  {
    tag: "Name",
    name: "name",
    type: "text",
    defaultValue: form.name,
    isRequired: true,
    error: form.nameError,
    onChange: form.handleNameChange,
    validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 50,  
        message: "Name must be between 3 and 50 characters."
      }
    ],
  },
  {
    tag: "Surname",
    name: "surname",
    type: "text",
    defaultValue: form.surname,
    isRequired: true,
    error: form.surnameError,
    onChange: form.handleSurnameChange,
    validators: [
      { 
        validate: (value) => value && value.length >= 3 && value.length <= 50,
        message: "Surname must be between 3 and 50 characters."
      }
    ],  
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: form.email,
    isRequired: true,
    error: form.emailError,
    onChange: form.handleEmailChange,
    validators: [
      {
        validate: (value) => /\S+@\S+\.\S+/.test(value),
        message: "El email no es válido"
      }
    ]
  },
  {
    tag: "Birthdate",
    name: "birthdate",
    type: "date",
    defaultValue: form.birthdate,
    isRequired: true,
    error: form.birthdateError,
    onChange: form.handleBirthdateChange,
    validators: [
      {
        validate: (value) => !!value,
        message: "La fecha de nacimiento es obligatoria"
      },
      {
        validate: (value) => {
          if (!value) return false;
          const today = new Date();
          const birthDate = new Date(value);
          today.setHours(0,0,0,0);
          birthDate.setHours(0,0,0,0);
          return birthDate < today;
        },
        message: "La fecha debe ser anterior a hoy"
      }
    ]
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
