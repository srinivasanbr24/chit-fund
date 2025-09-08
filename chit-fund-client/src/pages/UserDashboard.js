import { useEffect, useState } from "react";
import api from "../api/axios";
import { useLocation } from "react-router-dom";

const UserDashboard = () => {
  const [schemas, setSchemas] = useState([]);
  const {mobile} = useLocation().state;
  console.log(" Mobile Number is "+mobile);

  useEffect(() => {
    fetchUserSchemas();
  }, []);

  const fetchUserSchemas = async () => {
    const res = await api.get(`/user/schemas/${mobile}`);
    setSchemas(res.data);
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>User Dashboard</h2>
      <h3>My Schemas</h3>
      { schemas.length > 0 ? (
        <table className="schemas-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Durations (Months)</th>
              <th>Monthly Contribution</th>
            </tr>
          </thead>
          <tbody>
            {schemas.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.schemaName}</td>
                <td>{s.description}</td>
                <td>{s.durationInMonths}</td>
                <td>{s.monthlyContribution}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p> No Schema Found.</p>
      )}
    </div>
  );
};

export default UserDashboard;
