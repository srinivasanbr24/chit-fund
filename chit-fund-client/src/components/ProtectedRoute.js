import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { auth } = useContext(AuthContext);

  if (!auth) {
    // Not logged in → redirect to login
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(auth.user.role)) {
    // Logged in but role not allowed → redirect
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
