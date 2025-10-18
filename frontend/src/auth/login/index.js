import React, { useState } from "react";
import { Alert, Container } from "reactstrap";
import FormGenerator from "../../components/formGenerator/formGenerator";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { loginFormInputs } from "./form/loginFormInputs";

export default function Login() {
  const [message, setMessage] = useState(null)
  const loginFormRef = React.createRef();      
  

  async function handleSubmit({ values }) {

    const reqBody = values;
    setMessage(null);
    await fetch("/api/v1/auth/signin", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      .then(function (response) {
        if (response.status === 200) return response.json();
        else return Promise.reject("Invalid login attempt");
      })
      .then(function (data) {
        tokenService.setUser(data);
        tokenService.updateLocalAccessToken(data.token);
        window.location.href = "/dashboard";
      })
      .catch((error) => {         
        setMessage(error);
      });            
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
        {
          message != null && (
            <Alert color="primary">
              {message}
            </Alert>
          )
        }
        <h1 className="text-center" style={{
          fontWeight: 800,
          letterSpacing: "2px",
          color: "#FE5B02",
          textShadow: "0 2px 8px #000"
        }}>
          Login
        </h1>
        <div className="auth-form-container">
          <FormGenerator
            ref={loginFormRef}
            inputs={loginFormInputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            listenEnterKey
            buttonText="Login"
            buttonClassName="auth-button orange-button"
          />
        </div>
      </Container>
    </div>
  );  
}