import React, { useState } from 'react';
import '../css/UserModal.css' // Reuse the same modal styles

const AddUserModal = ({ onAddUser, onClose }) => {
  const [formData, setFormData] = useState({
    mobileNumber: '',
    userName: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onAddUser(formData); // Pass the form data back to the parent
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>Add New User</h3>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        <div className="modal-body">
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="mobileNumber">Mobile Number</label>
              <input
                type="text"
                id="mobileNumber"
                name="mobileNumber"
                value={formData.mobileNumber}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="userName">Username</label>
              <input
                type="text"
                id="userName"
                name="userName"
                value={formData.userName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-actions">
              <button type="submit" className="submit-button">Add User</button>
              <button type="button" onClick={onClose} className="cancel-button">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddUserModal;