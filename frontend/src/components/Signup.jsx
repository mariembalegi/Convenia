import React from 'react';

function Signup({ formData, handleInputChange, handleSubmit, toggleForm }) {
  const roles = [
    { value: 'ENSEIGNANT_CHERCHEUR', label: 'Enseignant Chercheur' },
    { value: 'DRI', label: 'DRI' },
    { value: 'CA', label: 'CA' },
    { value: 'CEVU', label: 'CEVU' },
    { value: 'DEVE', label: 'DEVE' }
  ];

  return (
    <div className="form-wrapper">
      <h1 className="form-title">Get Started Now</h1>
      <div className="form-content">
        <div className="input-group">
          <label className="input-label">Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            className="input-field"
            placeholder="Enter your name"
          />
        </div>
        <div className="input-group">
          <label className="input-label">Email address</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            className="input-field"
            placeholder="Enter your email"
          />
        </div>
        <div className="input-group">
          <label className="input-label">Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            className="input-field"
            placeholder="Enter your password"
          />
        </div>
        <div className="input-group">
          <label className="input-label">Role</label>
          <select
            name="role"
            value={formData.role}
            onChange={handleInputChange}
            className="input-field"
          >
            <option value="">Select your role</option>
            {roles.map(role => (
              <option key={role.value} value={role.value}>
                {role.label}
              </option>
            ))}
          </select>
        </div>
        <div className="checkbox-container">
          <input
            type="checkbox"
            name="agreeTerms"
            checked={formData.agreeTerms}
            onChange={handleInputChange}
            id="terms"
            className="checkbox-input"
          />
          <label htmlFor="terms" className="checkbox-label">
            I agree to the terms & policy
          </label>
        </div>
        <button onClick={handleSubmit} className="submit-btn">
          Sign up
        </button>


        <p className="switch-text">
          Have an account? <button onClick={toggleForm} className="switch-link">Sign In</button>
        </p>
      </div>
    </div>
  );
}

export default Signup;