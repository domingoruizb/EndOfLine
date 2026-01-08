import { formValidators } from "../../../validators/formValidators";

const userFormInputs = (auths, user = {}, isEdit = false) => {
  const inputs = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: user.username || "",
    isRequired: true,
    disabled: isEdit,
    validators: [
      {
        validate: (value) => value && value.length >= 3 && value.length <= 20,
        message: "El tamaño debe estar entre 3 y 20"
      }
    ]
  },
  ...(!isEdit ? [{
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
  }] : []),
  {
    tag: "Name",
    name: "name",
    type: "text",
    defaultValue: user.name || "",
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
    defaultValue: user.surname || "",
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
    defaultValue: user.email || "",
    isRequired: true,
    validators: [
      {
        validate: (value) => /\S+@\S+\.\S+/.test(value),
        message: "El email no es válido"
      }
    ]
  },
  {
    tag: "BirthDate",
    name: "birthdate",
    type: "date",
    defaultValue: user.birthdate || "",
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
    tag: "Authority",
    name: "authority",
    type: "select",
    defaultValue: user.authority?.id || "",
    isRequired: true,
    values: auths.map(auth => ({ value: auth.id, label: auth.type })),
  },
  ];
  return inputs;
};

export default userFormInputs;
