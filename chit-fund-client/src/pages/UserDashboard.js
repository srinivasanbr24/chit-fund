import { useEffect, useState } from "react";
import api from "../api/axios";

const UserDashboard = () => {
  const [schemas, setSchemas] = useState([]);

  useEffect(() => {
    fetchUserSchemas();
  }, []);

  const fetchUserSchemas = async () => {
    const res = await api.get("/users/me/schemas");
    setSchemas(res.data);
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>User Dashboard</h2>
      <h3>My Schemas</h3>
      <ul>
        {schemas.map((s) => (
          <li key={s.id}>{s.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default UserDashboard;
