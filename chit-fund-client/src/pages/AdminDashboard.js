import { useState, useEffect } from "react";
import api from "../api/axios";
import "../css/AdminDashboard.css";
import UserModal from "../components/UserModal";
import CreateSchemaModal from "../components/CreateSchemaModal";
import AddUserModal from "../components/AddUserModal";

const AdminDashboard = () => {
  const [schemas, setSchemas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showUserModal, setShowUserModal] = useState(false);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [showCreateSchemaModal, setShowCreateSchemaModal] = useState(false);
  const [schemaId, setSchemaId] = useState(null);
  const [showAddUserModal, setShowAddUserModal] = useState(false);
  const [addUserSchemaId, setAddUserSchemaId] = useState(null);

  useEffect(() => {
    fetchSchemas();
  }, []);

  const fetchSchemas = async () => {
    setLoading(true);
    try {
      const res = await api.get("admin/schemas");
      if (Array.isArray(res.data)) {
        setSchemas(res.data);
      } else {
        console.error("API response is not an array:", res.data);
        setSchemas([]);
      }
      console.log(res.data);
    } catch (err) {
      console.error("Failed to fetch schemas:", err);
      setSchemas([]);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenCreateSchemaModal = () => {
    setShowCreateSchemaModal(true);
  };

  const handleCloseCreateSchemaModal = () => {
    setShowCreateSchemaModal(false);
  };

  const handleCreateSchema = async (schemaData) => { 
    try {
      await api.post("admin/schemas/create", schemaData);
      handleCloseCreateSchemaModal(); 
      fetchSchemas();
    } catch (err) {
      console.error("Failed to create schema:", err);
      alert("Error creating schema. Please try again.");
    }
  };

 
 const handleSeeUsers = (users,id) => {
    setSelectedUsers(users); // Set the users for the modal
    setShowUserModal(true); // Open the modal
    setSchemaId(id)
  };

  const handleAddUser = async (schemaId) => {
    setAddUserSchemaId(schemaId);
    setShowAddUserModal(true); // Open the Add User modal
    console.log(`Adding a new user to schema ID: ${schemaId}`);
  };

  const handleCloseUserModal = () => {
    setShowUserModal(false); // Close the modal
    setSelectedUsers([]); // Clear the selected users
    setSchemaId(null);
  };

  const handleAddUserSubmit = async (userData) => {
    try {
      await api.post(`admin/schemas/${addUserSchemaId}/addUser`, userData);
      handleCloseAddUserModal(); 
      fetchSchemas(); 
    } catch (err) {
      console.error("Failed to add user:", err);
      alert("Error adding user. Please try again.");
    }
  };

  const handleCloseAddUserModal = () => {
    setShowAddUserModal(false);
    setAddUserSchemaId(null);
  };

  const handleRemoveSchema = async (schemaId) => {
    window.confirm("Are you sure you want to remove this schema?");
    try {
      await api.delete(`admin/schemas/${schemaId}/delete`);
      fetchSchemas(); 
    } catch (err) {
      console.error("Failed to remove schema:", err);
      alert("Error removing schema. Please try again.");
    }
  };

  const handleRemoveUser = async (mobileNumber) => {
    if (window.confirm("Are you sure you want to remove this user from the schema?")) {
      try {
        await api.delete(`admin/schemas/${schemaId}/removeUser/${mobileNumber}`);
        await fetchSchemas(); 
        const updatedSchemas = await api.get("admin/schemas");
        const updatedSchema = updatedSchemas.data.find(s => s.id === schemaId);
        if (updatedSchema) {
          setSelectedUsers(updatedSchema.users);
        }

      } catch (err) {
        console.error("Failed to remove user:", err);
        alert("Error removing user. Please try again.");
      }
    }
  };
  

  return (
    <div className="admin-dashboard-container">
      <h1>Admin Dashboard</h1>

      <button onClick={handleOpenCreateSchemaModal}>Create New Schema</button>

      <h3>Available Schemas</h3>
      {loading ? (
        <p>Loading schemas...</p>
      ) : schemas.length > 0 ? (
        <table className="schemas-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Durations (Months)</th>
              <th>Monthly Contribution</th>
              <th>Users</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {schemas.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.name}</td>
                <td>{s.description}</td>
                <td>{s.durationInMonths}</td>
                <td>{s.monthlyContribution}</td>
                <td><button onClick={() => handleSeeUsers(s.users,s.id)}>View & Add Users ({s.users.length})</button></td>
                <td>
                  <button onClick={() => handleRemoveSchema(s.id)}>Remove Schema</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p> No Schema Found.</p>
      )}
      {showUserModal && (
        <UserModal 
          users={selectedUsers} 
          onClose={handleCloseUserModal} 
          handleAddUser={handleAddUser} 
          schemaId={schemaId}
          handleRemoveUser={handleRemoveUser}/>
      )}  

      {showCreateSchemaModal && (
        <CreateSchemaModal
          onCreateSchema={handleCreateSchema}
          onClose={handleCloseCreateSchemaModal}
        />
      )}

      {showAddUserModal && (
        <AddUserModal
          onAddUser={handleAddUserSubmit}
          onClose={handleCloseAddUserModal}
        />
      )}

    </div>
  );
};

export default AdminDashboard;