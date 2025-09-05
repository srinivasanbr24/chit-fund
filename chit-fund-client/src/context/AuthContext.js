import { createContext, useState } from "react";
import {jwtDecode} from "jwt-decode";

export const AuthContext = createContext();

const decodeToken = (token) => {
  try {
   // Check if the token is a non-empty string before decoding
    if (typeof token === 'string' && token.trim() !== '') {
      return jwtDecode(token);
    }
  } catch (error) {
    console.error("Failed to decode token:", error);
  }
  return null;
};
  

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(() => {
    const token = localStorage.getItem("token");
    const user = decodeToken(token);
    return token ? { token, user} : null;
  });

  const login = (token) => {
    const user = decodeToken(token);
    if (user) {
      localStorage.setItem("token", token);
      setAuth({ token, user });
    } else {
      console.error("Login failed: Invalid token");
      logout();
      return;
    }
    localStorage.setItem("token", token);
    setAuth({ token, user: jwtDecode(token) });
  };

  const logout = () => {
    localStorage.removeItem("token");
    setAuth(null);
  };

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
