import { formValidators } from "../../../validators/formValidators";

export const registerFormPlayer  = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 20,
        message: "El tamaño debe estar entre 3 y 20"
      }
    ]
  },
  {
    tag: "First Name",
    name: "name",
    type: "text",
    defaultValue: "",
    isRequired: true,
     validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 50,
        message: "El tamaño debe estar entre 3 y 50"
      }
    ]
  },
    {
    tag: "Surname",
    name: "surname",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 50,
        message: "El tamaño debe estar entre 3 y 50"
      }
    ]
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [
      {
        validate: (value) => /\S+@\S+\.\S+/.test(value),
        message: "El email no es válido"
      }
    ]
  },
  {
    tag: "Password",
    name: "password",
    type: "password",
    defaultValue: "",
    isRequired: true,
    validators: [
      {
        validate: (value) => value && value.length >= 5 && value.length <= 40,
        message: "El tamaño debe estar entre 5 y 40"
      }
    ]
  },
  {
    tag: "Birth Date",
    name: "birthdate",
    type: "date",
    defaultValue: "",
    isRequired: true,
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
    defaultValue: "https://cdn.iconscout.com/icon/free/png-256/free-avatar-icon-svg-download-png-456324.png",
    isRequired: false,
    validators: [formValidators.notBlankValidator],
  },
];
