// Configuración de inputs reutilizable para formularios de logros (crear y editar)
const achievementFormInputs = (achievement = {}) => [
  {
    tag: "Name",
    name: "name",
    type: "text",
    defaultValue: achievement.name || "",
    isRequired: true,
  },
  {
    tag: "Description",
    name: "description",
    type: "text",
    defaultValue: achievement.description || "",
    isRequired: true,
  },
  {
    tag: "Badge Image Url",
    name: "badgeImage",
    type: "text",
    defaultValue: achievement.badgeImage || "",
    isRequired: false,
  },
  {
    tag: "Category",
    name: "category",
    type: "select",
    defaultValue: achievement.category || "GAMES_PLAYED",
    isRequired: true,
    values: [
      { value: "GAMES_PLAYED", label: "GAMES_PLAYED" },
      { value: "VICTORIES", label: "VICTORIES" },
      { value: "TOTAL_PLAY_TIME", label: "TOTAL_PLAY_TIME" },
    ],
  },
  {
    tag: "Threshold value:",
    name: "threshold",
    type: "number",
    defaultValue: achievement.threshold || 1,
    isRequired: true,
  },
];

export default achievementFormInputs;
