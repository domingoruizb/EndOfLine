// Configuración de inputs reutilizable para formularios de logros (crear y editar)
const achievementFormInputs = (achievement = {}) => [
  {
    tag: "Name",
    name: "name",
    type: "text",
    defaultValue: achievement.name || "",
    isRequired: true,
    validators: [
      {
        validate: (value) => typeof value === "string" && value.length >= 3,
        message: "Name must be at least 3 characters."
      },
      {
        validate: (value) => typeof value === "string" && value.length <= 30,
        message: "Name must be at most 30 characters."
      }
    ],
  },
  {
    tag: "Description",
    name: "description",
    type: "text",
    defaultValue: achievement.description || "",
    isRequired: true,
    validators: [
      {
        validate: (value) => typeof value === "string" && value.length >= 5,
        message: "Description must be at least 5 characters."
      },
      {
        validate: (value) => typeof value === "string" && value.length <= 100,
        message: "Description must be at most 100 characters."
      }
    ],
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
    tag: "Threshold value",
    name: "threshold",
    type: "number",
    defaultValue: achievement.threshold,
    isRequired: true,
    validators: [
      {
        validate: (value) => Number(value) >= 1,
        message: "Threshold must be at least 1."
      }
    ],
  },
];

export default achievementFormInputs;
