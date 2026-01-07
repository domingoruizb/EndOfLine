const userFormInputs = (auths, user = {}, isEdit = false) => {
  const inputs = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: user.username || "",
    isRequired: true,
    disabled: isEdit,
  },
  ...(!isEdit ? [{
    tag: "Password",
    name: "password",
    type: "password",
    defaultValue: "",
    isRequired: true,
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
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: user.email || "",
    isRequired: true,
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
      }
    ]
  },
  {
    tag: "Authority",
    name: "authority",
    type: "select",
    defaultValue: user.authority?.id || "",
    isRequired: true,
    values: auths.map(auth => ({ value: auth.id, label: auth.authority })),
  },
  ];
  return inputs;
};

export default userFormInputs;
