import React from "react";
import { Link } from "react-router-dom";
import "./static/css/error-page.css";

export default function ErrorPage() {
  return (
    <div className="error-page-container">
      <h1 className="error-title">404</h1>
      <h2 className="error-subtitle">Page Not Found</h2>
      <p className="error-message">The page you are looking for does not exist.</p>
      <Link to="/" className="error-home-link">Go to Home</Link>
    </div>
  );
}