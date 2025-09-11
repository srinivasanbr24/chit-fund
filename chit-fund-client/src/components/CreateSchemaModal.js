import React, { useState } from 'react';
import '../css/UserModal.css';

const CreateSchemaModal = ({ onCreateSchema, onClose }) => {
  const [formData, setFormData] = useState({
    schemaName: '',
    description: '',
    durationInMonths: '',
    monthlyContribution: ''
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
    onCreateSchema(formData); // Pass the form data back to the parent
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>Create New Schema</h3>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        <div className="modal-body">
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="schemaName">Schema Name</label>
              <input
                type="text"
                id="schemaName"
                name="schemaName"
                value={formData.schemaName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="description">Description</label>
              <input
                type="text"
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="durationInMonths">Duration (Months)</label>
              <input
                type="number"
                step="1"
                id="durationInMonths"
                name="durationInMonths"
                value={formData.durationInMonths}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="monthlyContribution">Monthly Contribution</label>
              <input
                type="number"
                step="100"
                id="monthlyContribution"
                name="monthlyContribution"
                value={formData.monthlyContribution}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-actions">
              <button type="submit" className="submit-button">Create</button>
              <button type="button" onClick={onClose} className="cancel-button">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateSchemaModal;