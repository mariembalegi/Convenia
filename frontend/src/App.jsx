import { useState } from 'react';
import './App.css';
import Login from './components/Login';
import Signup from './components/Signup';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: '',
    agreeTerms: false
  });

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async () => {
    const endpoint = isLogin ? '/api/auth/login' : '/api/auth/signup';
    const payload = isLogin 
      ? { email: formData.email, password: formData.password }
      : { name: formData.name, email: formData.email, password: formData.password, role: formData.role };

    try {
      const response = await fetch(`http://localhost:8081${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      
      const data = await response.json();
      
      if (response.ok) {
        alert(`${isLogin ? 'Connexion' : 'Inscription'} réussie!\nBienvenue ${data.name || formData.name}!`);
        console.log('Données utilisateur:', data);
      } else {
        alert(data.message || 'Une erreur est survenue');
      }
    } catch (error) {
      console.error('Erreur:', error);
      alert('Erreur de connexion au serveur');
    }
  };

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setFormData({ name: '', email: '', password: '', role: '', agreeTerms: false });
  };

  return (
    <div className="app-background">
      <div className="popup-container">
        <div className="form-section">
          {isLogin ? (
            <Login
              formData={formData}
              handleInputChange={handleInputChange}
              handleSubmit={handleSubmit}
              toggleForm={toggleForm}
            />
          ) : (
            <Signup
              formData={formData}
              handleInputChange={handleInputChange}
              handleSubmit={handleSubmit}
              toggleForm={toggleForm}
            />
          )}
        </div>
        <div className="design-section">
          <div className="shape green-shape-1"></div>
          <div className="shape green-shape-2"></div>
          <div className="shape white-shape-1"></div>
          <div className="shape black-shape-1"></div>
          <div className="shape black-shape-2"></div>
          <div className="shape white-shape-2"></div>
          <div className="shape white-shape-3"></div>
          <div className="shape black-shape-3"></div>
          <div className="shape black-shape-4"></div>
          <div className="shape green-shape-3"></div>
          <div className="shape green-shape-4"></div>
          <div className="shape green-shape-5"></div>
          <div className="shape green-shape-6"></div>
        </div>
      </div>
    </div>
  );
}

export default App;