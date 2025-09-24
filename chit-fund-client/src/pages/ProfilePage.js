import React, { useContext } from "react";
import { AuthContext } from "../context/AuthContext";


const ProfilePage = () =>{
    const { auth } = useContext(AuthContext);

return (
    <div style={{ padding: "2rem" }}>
      <h1>My Profile</h1>
      <div style={{ marginBottom: "2rem" }}>
        <p>
          <strong>Username:</strong> {auth?.user?.username || "N/A"}
        </p>
        <p>
          <strong>Mobile Number:</strong> {auth?.user?.sub || "N/A"}
        </p>
      </div>
      </div>
  );
};
export default ProfilePage;