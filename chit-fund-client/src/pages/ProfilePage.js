import React, { useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import api from "../api/axios";
import "../css/ProfilePage.css";


const ProfilePage = () =>{
    const { auth } = useContext(AuthContext);
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");

    const handlePasswordUpdate = async (e) => {
        e.preventDefault();
        setMessage("");

        if(newPassword !== confirmPassword){
            setMessage("password do not match.");
            return;
        }

        try{
            const mobileNumber = auth.user.sub;
            const res = await api.patch(`/user/updatePassword/${mobileNumber}`, {
                oldPassword: oldPassword,
                newPassword : newPassword
            });

            if(res.data === true){
                setMessage("Password updated successfully!");
                setOldPassword("");
                setNewPassword("");
                setConfirmPassword("");
            } else {
                setMessage(" Failed to update password. Please check your old");
            }
        } catch (err){
            console.error("Password update failed: ",err);
            setMessage("Error updating password. Please try again");
        }
    };
 return (
    <div className="profile-container">
      <div className="profile-card">
        <h1>My Profile</h1>
        <div className="user-info">
          <p>
            <strong>Username:</strong> {auth?.user?.username || "N/A"}
          </p>
          <p>
            <strong>Mobile Number:</strong> {auth?.user?.sub || "N/A"}
          </p>
        </div>

        <hr />

        <h3>Update Password</h3>
        <form onSubmit={handlePasswordUpdate} className="password-form">
          <div className="form-group">
            <label htmlFor="oldPassword">Old Password:</label>
            <input
              type="password"
              id="oldPassword"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="newPassword">New Password:</label>
            <input
              type="password"
              id="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password:</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit">Update Password</button>
        </form>
        {message && <p className={message.includes("success") ? "success-message" : "error-message"}>{message}</p>}
      </div>
    </div>
  );
};
export default ProfilePage;