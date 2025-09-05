import React from 'react';
import '../css/UserModal.css';

const UserModal = ({ users, onClose , handleAddUser,schemaId}) => {
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
                  <th>Username</th>
                  <th>Mobile Number</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user, index) => (
                  <tr key={index}>
                    <td>{user.userName || 'N/A'}</td>
                    <td>{user.mobileNumber || 'N/A'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No users found for this schema.</p>
          )}
          <button onClick={() => handleAddUser(schemaId)}>Add User</button>
        </div>
      </div>
    </div>
  );
};

export default UserModal;