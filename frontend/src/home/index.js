import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import logo from '../static/images/EndOfLineProjectLogo.png';
import { useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';



export default function Home(){
    const location = useLocation();
    const [message, setMessage] = useState("");

    useEffect(() => {
    if (location.state?.message) {
      setMessage(location.state.message);
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

    return(
        <>
        {message && (
            <div className="message">
                {message}
            </div>
        )}
        <div className="home-page-container">
            <div className="hero-div">
                <h1>END OF LINE</h1>
                <img src={logo} alt="Logo" style={{ width: '500px', height: 'auto', borderRadius: '50px' }} />
            </div>
        </div>
        </>
    );
}