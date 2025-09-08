import React, { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import '../css/LogoutButton.css';

const LogoutButton = () => {
  const { logout } = useContext(AuthContext);

  return (
    <button className="logout-button" onClick={logout}>Logout</button>
  );
};

export default LogoutButton;