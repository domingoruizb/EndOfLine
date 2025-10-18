import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import logo from '../static/images/EndOfLineProjectLogo.png';



export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div">
                <h1>END OF LINE</h1>
                <img src={logo} alt="Logo" style={{ width: '500px', height: 'auto', borderRadius: '50px' }} />
            </div>
        </div>
    );
}