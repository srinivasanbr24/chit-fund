import React, {useState} from 'react';
import '../css/UserModal.css';

const UserModal = ({ users, onClose , handleAddUser,schemaId, handleRemoveUser}) => {
    const [selectedUser, setSelectedUser] = useState(null);

    const handleSelectionChange = (e) => {
    const selectedMobileNumber = e.target.value;
    const user = users.find(u => u.mobileNumber === selectedMobileNumber);
    setSelectedUser(user);
  };

  const handleRemoveClick = async () => {
    if (selectedUser) {
        await handleRemoveUser(selectedUser.mobileNumber);
        setSelectedUser(null); 
    }

  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>Users List</h3>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        <div className="modal-body">
          {users.length > 0 ? (
            <table className="users-table">
              <thead>
                <tr>
                  <th>S.No</th>
                  <th>Name</th>
                  <th>Mobile Number</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user, index) => (
                  <tr key={index}>
                    <td>
                      <input
                        type="radio"
                        name="userSelection"
                        value={user.mobileNumber}
                        checked={selectedUser && selectedUser.mobileNumber === user.mobileNumber}
                        onChange={handleSelectionChange}
                      />
                      </td>
                    <td>{user.userName || 'N/A'}</td>
                    <td>{user.mobileNumber || 'N/A'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No users found for this schema.</p>
          )}
            <button onClick={handleRemoveClick} disabled={!selectedUser}>Remove User</button>
          <button onClick={() => handleAddUser(schemaId)}>Add User</button>
        </div>
      </div>
    </div>
  );
};

export default UserModal;