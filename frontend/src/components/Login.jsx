import React from 'react';

function Login({ formData, handleInputChange, handleSubmit, toggleForm }) {
  return (
    <div className="form-wrapper">
      <h1 className="form-title">Welcome back!</h1>
      <p className="form-subtitle">Enter your Credentials to access your account</p>
      <div className="form-content">
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
          <div className="password-header">
            <label className="input-label">Password</label>
            <a href="#" className="forgot-link">Forgot password?</a>
          </div>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            className="input-field"
            placeholder="Enter your password"
          />
        </div>
        <div className="checkbox-container">
          <input
            type="checkbox"
            name="agreeTerms"
            checked={formData.agreeTerms}
            onChange={handleInputChange}
            id="remember"
            className="checkbox-input"
          />
          <label htmlFor="remember" className="checkbox-label">
            Remember for 30 days
          </label>
        </div>
        <button onClick={handleSubmit} className="submit-btn">
          Login
        </button>

        <p className="switch-text">
          Don't have an account? <button onClick={toggleForm} className="switch-link">Sign Up</button>
        </p>
      </div>
    </div>
  );
}

export default Login;