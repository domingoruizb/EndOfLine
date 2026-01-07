const userFormInputs = (auths, user = {}, isEdit = false) => [
  {
    label: "Username",
    name: "username",
    type: "text",
    defaultValue: user.username || "",
    isRequired: true,
    disabled: isEdit,
  },
  {
    label: "Name",
    name: "name",
    type: "text",
    defaultValue: user.name || "",
    isRequired: true,
  },
  {
    label: "Surname",
    name: "surname",
    type: "text",
    defaultValue: user.surname || "",
    isRequired: true,
  },
  {
    label: "Email",
    name: "email",
    type: "text",
    defaultValue: user.email || "",
    isRequired: true,
  },
  {
    label: "BirthDate",
    name: "birthdate",
    type: "date",
    defaultValue: user.birthdate || "",
    isRequired: true,
  },
  {
    label: "Authority",
    name: "authority",
    type: "select",
    defaultValue: user.authority?.id || "",
    isRequired: true,
    values: auths.map(auth => ({ value: auth.id, label: auth.authority })),
  },
];

export default userFormInputs;
