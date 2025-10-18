import { formValidators } from "../../../validators/formValidators";

export const registerFormPlayer = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "First Name",
    name: "name",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "Last Name",
    name: "surname",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.validEmailValidator],
  },
    {
    tag: "Password",
    name: "password",
    type: "password",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "Birth Date",
    name: "birthdate",
    type: "date",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notNullValidator, formValidators.validDateValidator]
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
