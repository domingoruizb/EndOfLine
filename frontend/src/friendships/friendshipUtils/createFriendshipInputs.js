// Configuración de inputs para el formulario de crear amistad
const createFriendshipInputs = (username = "") => [
  {
    label: "Username",
    name: "username",
    type: "text",
    defaultValue: username,
    isRequired: true,
  },
];

export default createFriendshipInputs;
