import { useState } from "react";
import { Link } from "react-router-dom";
import UserForm from "./adminComponents/UserForm";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import "../../static/css/admin/userEditAdmin.css";
import "../../static/css/admin/userListAdmin.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserEditAdmin() {
  const emptyItem = {
    id: null,
    username: "",
    name: "",
    surname: "",
    password: "",
    birthdate: "", 
    email: "",
    authority: null,
  };
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [user, setUser] = useFetchState(
    emptyItem,
    `/api/v1/users/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const auths = useFetchData(`/api/v1/users/authorities`, jwt);

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "authority") {
      const auth = auths.find((a) => a.id === Number(value));
      setUser({ ...user, authority: auth });
    } else setUser({ ...user, [name]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();
    const values = event.values || (event.target && event.target.elements) || {};
    let userToSend = values.values ? values.values : values;
    if (!userToSend.id && user && user.id) {
      userToSend.id = user.id;
    }
    if ((!userToSend.password || userToSend.password === "") && userToSend.id) {
      userToSend.password = userToSend.password || user.password;
    }
    fetch("/api/v1/users" + (userToSend.id ? "/" + userToSend.id : ""), {
      method: userToSend.id ? "PUT" : "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userToSend),
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else window.location.href = "/users";
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);
  const authOptions = auths.map((auth) => (
    <option key={auth.id} value={auth.id}>
      {auth.authority}
    </option>
  ));

  return (
    <div className="user-list-page">
      <div className="admin-page-container user-list-container">
      {<h1 className="text-center user-list-title">{user.id ? "Edit User" : "Add User"}</h1>}
      {modal}
      <div className="auth-form-container">
        <UserForm
          user={user}
          onChange={handleChange}
          onSubmit={handleSubmit}
          auths={auths}
          isEdit={!!user.id}
        />
      </div>
    </div>
    </div>
    
  );
}
