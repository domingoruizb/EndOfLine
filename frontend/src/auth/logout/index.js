import tokenService from "../../services/token.service";
import LinkClickButton from "../../components/LinkClickButton";

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
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">
          Logout
        </h1>
        <div className="form-container no-bg">
          <p className="text-white text-center">
            Are you sure you want to log out?
          </p>
          <div className="buttons-container">
            <LinkClickButton
              link="/"
              text="NO"
              className='danger'
            />
            <LinkClickButton
              onClick={() => sendLogoutRequest()}
              text="YES"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Logout;
