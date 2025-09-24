import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { useContext } from "react";
import LogoutButton from "./components/LogoutButton";
import { AuthProvider } from "./context/AuthContext";
import { AuthContext } from "./context/AuthContext";
import LoginPage from "./pages/LoginPage";
import AdminDashboard from "./pages/AdminDashboard";
import UserDashboard from "./pages/UserDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import ProfileIcon from "./components/ProfileIcon";
import ProfilePage from "./pages/ProfilePage";
function App() {
  return (

    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

function AppContent() {
  const { auth } = useContext(AuthContext);

  return (
    <>
      <header style={{ 
        padding: "10px",
        backgroundColor: "#FFC107",
        color: "#333",
        marginBottom: "20px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
      }}>
        <h1 style={{margin: "0"}}>AKB Chit Fund </h1>
        {auth && 
        (<div style={{ display: 'flex', alignItems: 'center' }}>
          <Link to= "/profile">
            <ProfileIcon />
          </Link>
            <LogoutButton />
          </div>)
        }
      </header>
      <main>
        <Routes> 
          <Route path="/" element={<LoginPage />} />
          <Route path="/admin" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminDashboard />
            </ProtectedRoute>
          } />
          <Route path="/user" element={
            <ProtectedRoute allowedRoles={["USER"]}>
              <UserDashboard />
            </ProtectedRoute>
          } />
          <Route path="/profile" element={
            <ProtectedRoute allowedRoles={["ADMIN", "USER"]}>
              <ProfilePage />
            </ProtectedRoute>
          } />
        </Routes>
      </main>
    </>
  );
}

export default App;
