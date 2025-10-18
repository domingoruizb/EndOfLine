import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";
import { Container } from 'reactstrap';

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
    <div
      style={{
        backgroundColor: "black",
        color: "white",
        minHeight: "100vh",
        padding: "2rem 0",
        fontFamily: "Inter, Arial, sans-serif",
      }}
    >
      <Container
        className="auth-page-container"
        style={{
          padding: "0 1rem",
          maxWidth: 900,
          background: "none",
          borderRadius: "1rem",
          boxShadow: "none",
        }}
      >
        <h1 className="text-center" style={{
          fontWeight: 800,
          letterSpacing: "2px",
          color: "#FE5B02",
          textShadow: "0 2px 8px #000"
        }}>
          Logout
        </h1>
        <div className="auth-form-container">
          <h2 className="text-center text-md">
            Are you sure you want to log out?
          </h2>
          <div className="options-row">
            <Link className="auth-button" to="/" style={{textDecoration: "none"}}>
              No
            </Link>
            <button className="auth-button orange-button" onClick={() => sendLogoutRequest()}>
              Yes
            </button>
          </div>
        </div>
      </Container>
    </div>
  );
};

export default Logout;
