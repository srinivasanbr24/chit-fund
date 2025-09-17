import React, { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

const ProfileIcon = () => {
  const { auth } = useContext(AuthContext);

  // Get the first letter of the username or mobile number as a fallback
  const firstLetter = auth?.user?.username?.charAt(0).toUpperCase() || auth?.user?.sub?.charAt(0).toUpperCase() || '?';

  return (
    <div style={{
      width: '40px',
      height: '40px',
      borderRadius: '50%',
      backgroundColor: '#007BFF', // A pleasant blue color
      color: 'white',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      fontSize: '1.2rem',
      fontWeight: 'bold',
      marginRight: '10px' // Add some space between the icon and the button
    }}>
      {firstLetter}
    </div>
  );
};

export default ProfileIcon;