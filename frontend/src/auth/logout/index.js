import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import '../../static/css/home/home.css';
import "../../static/css/auth/logout.css"; 
import tokenService from "../../services/token.service";

const Logout = () => {
  function sendLogoutRequest() {
    const jwt = window.localStorage.getItem("jwt");
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  return (
    <div className="logout-page">
      <div className="logout-content-wrapper">
        <h1 className="logout-title">
          Logout
        </h1>
        <div className="logout-form-container">
          <h2 className="logout-message">
            Are you sure you want to log out?
          </h2>
          <div className="logout-buttons">
            <Link className="logout-button" to="/">
              No
            </Link>
            <button className="logout-button logout-button-confirm" onClick={() => sendLogoutRequest()}>
              Yes
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Logout;
