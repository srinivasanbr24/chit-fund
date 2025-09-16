import { useEffect, useState, useContext } from "react";
import api from "../api/axios";
import { AuthContext } from "../context/AuthContext";

const UserDashboard = () => {
  const [schemas, setSchemas] = useState([]);
  const [loading, setLoading] = useState(true);
  const { auth } = useContext(AuthContext);

  useEffect(() => {
    if(auth && auth.user && auth.user.sub) {
      console.log("Fetching schemas for mobile:", auth.user.sub);
      fetchUserSchemas(auth.user.sub);
    } else {
      console.error("User information is missing in auth context.");
      setLoading(false);
    }
  }, [auth]);

  const fetchUserSchemas = async (mobile) => {
    try{
      const res = await api.get(`/user/schemas/${mobile}`);
      setSchemas(res.data);
    } catch(err) {
      console.error("Failed to fetch user schemas:", err);
      setSchemas([]);
    } finally {
      setLoading(false);
    }
  };

  if(loading) {
    return <p>Loading your Plans.....</p>;
  }

  if(!auth){
    return <p>Please login to view your Dashboard.</p>;
  }
  

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Welcome, {auth.user.username || auth.user.mobileNumber}!</h2>
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
