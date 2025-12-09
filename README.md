# CONVENIA - Full-Stack Integration Documentation

## 📋 Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Backend Configuration](#backend-configuration)
- [Frontend Configuration](#frontend-configuration)
- [API Endpoints](#api-endpoints)
- [Setup Instructions](#setup-instructions)
- [Testing the Integration](#testing-the-integration)
- [Troubleshooting](#troubleshooting)
- [Team Setup Guide](#team-setup-guide)

---

## 🎯 Overview

This document describes the integration between the J2EE backend and React frontend in the CONVENIA project. The application allows users to sign up, log in, and manage their sessions with a RESTful API architecture.

**Technology Stack:**
- **Backend:** J2EE (Jakarta EE), EJB, JPA/Hibernate, WildFly/JBoss
- **Frontend:** React 19, Vite
- **Database:** MySQL 8.x
- **API Style:** RESTful JSON

---

## 🏗️ Architecture

```
┌─────────────────┐         HTTP/JSON          ┌──────────────────┐
│  React Frontend │  ◄──────────────────────►  │  J2EE Backend    │
│  (Port 5173)    │      REST API Calls        │  (Port 8080)     │
└─────────────────┘                            └──────────────────┘
         │                                               │
         │ Vite Proxy                                   │
         │ /api → localhost:8080                        │
         │                                              ▼
         │                                     ┌─────────────────┐
         │                                     │  MySQL Database │
         │                                     │  (conveniadb)   │
         └─────────────────────────────────────┴─────────────────┘
```

**Key Components:**
1. **CORS Filter** - Handles cross-origin requests between frontend and backend
2. **UserServlet** - REST API endpoint for authentication operations
3. **EJB Services** - Business logic layer for user management
4. **JPA Entities** - Database models with Hibernate ORM
5. **React Components** - User interface for login/signup
6. **Vite Proxy** - Development proxy to avoid CORS issues

---

## ✅ Prerequisites

Before setting up the project, ensure you have:

- **Java Development Kit (JDK)** 11 or higher
- **WildFly/JBoss Application Server** (version 26+ recommended)
- **MySQL Server** 8.x
- **Node.js** 18+ and npm
- **IDE** (Eclipse, IntelliJ IDEA, or VS Code)
- **Git** for version control

---

## 📁 Project Structure

```
CONVENIA/
├── src/main/
│   ├── java/com/enit/
│   │   ├── Controllers/
│   │   │   └── UserServlet.java          # REST API Controller (Modified)
│   │   ├── filters/
│   │   │   └── CorsFilter.java           # CORS Handler (New)
│   │   ├── entities/
│   │   │   ├── User.java                 # User Entity (Modified - added email)
│   │   │   ├── Demande.java
│   │   │   ├── EtablissementPartenaire.java
│   │   │   ├── Notification.java
│   │   │   └── PieceJointe.java
│   │   └── service/
│   │       ├── UserService.java          # User Business Logic (Modified)
│   │       └── UserServiceLocal.java     # EJB Interface (Modified)
│   ├── resources/META-INF/
│   │   └── persistence.xml               # JPA Configuration (Modified)
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── convenia-ds.xml           # DataSource Configuration
│       │   └── web.xml                   # Servlet Configuration (Modified)
│       └── index.html
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Login.jsx                 # Login Component
│   │   │   └── Signup.jsx                # Signup Component
│   │   ├── App.jsx                       # Main App (Modified)
│   │   ├── App.css
│   │   ├── main.jsx
│   │   └── index.css
│   ├── vite.config.js                    # Vite Configuration (Modified)
│   ├── package.json
│   └── index.html
└── README.md                             # This file
```

---

## 🔧 Backend Configuration

### 1. Database Configuration (`convenia-ds.xml`)

Located at: `src/main/webapp/WEB-INF/convenia-ds.xml`

```xml
<datasources>
    <datasource jndi-name="java:/conveniaDS" pool-name="conveniaDS_Pool2">
        <connection-url>jdbc:mysql://localhost:3306/conveniadb</connection-url>
        <driver>mysql</driver>
        <security>
            <user-name>root</user-name>
            <password>your_password</password>
        </security>
    </datasource>
</datasources>
```

**⚠️ Important:** Update the MySQL credentials before deployment.

---

### 2. JPA Configuration (`persistence.xml`)

Located at: `src/main/resources/META-INF/persistence.xml`

**Changes Made:**
- Updated persistence unit name to `myPU`
- Changed datasource JNDI name to `java:/conveniaDS`
- Added all entity classes
- Configured Hibernate to auto-update schema

```xml
<persistence-unit name="myPU">
    <jta-data-source>java:/conveniaDS</jta-data-source>
    <class>com.enit.entities.User</class>
    <!-- Other entity classes -->
    <properties>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
    </properties>
</persistence-unit>
```

---

### 3. User Entity (`User.java`)

Located at: `src/main/java/com/enit/entities/User.java`

**Changes Made:**
- ✅ **Added `email` field** - Required for login functionality
- The email field is unique and non-nullable

```java
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;  // ADDED - was missing
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private boolean active = true;
    
    // Getters and setters...
}
```

---

### 4. User Service Interface (`UserServiceLocal.java`)

Located at: `src/main/java/com/enit/service/UserServiceLocal.java`

**Changes Made:**
- ✅ Added `findByEmail(String email)` method
- ✅ Added `loginByEmail(String email, String password)` method

```java
@Local
public interface UserServiceLocal {
    User login(String username, String password);
    User loginByEmail(String email, String password);  // NEW
    void create(User user);
    User update(User user);
    void delete(Long id);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);  // NEW
    List<User> findAll();
}
```

---

### 5. User Service Implementation (`UserService.java`)

Located at: `src/main/java/com/enit/service/UserService.java`

**Changes Made:**
- ✅ Implemented `findByEmail()` method
- ✅ Implemented `loginByEmail()` method

```java
@Stateless
public class UserService implements UserServiceLocal {
    
    @PersistenceContext(unitName = "myPU")
    private EntityManager em;
    
    @Override
    public User findByEmail(String email) {
        try {
            TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class
            );
            q.setParameter("email", email);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public User loginByEmail(String email, String password) {
        User u = findByEmail(email);
        if (u == null) return null;
        if (u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
    
    // Other methods...
}
```

**⚠️ Security Note:** In production, passwords should be hashed using BCrypt or similar algorithms.

---

### 6. REST API Controller (`UserServlet.java`)

Located at: `src/main/java/com/enit/Controllers/UserServlet.java`

**Changes Made:**
- ✅ **Complete rewrite** from JSP-style to RESTful API
- ✅ Changed URL mapping from `/auth` to `/api/auth/*`
- ✅ Implemented manual JSON parsing (no external dependencies)
- ✅ Added proper HTTP status codes and error handling
- ✅ Implemented session management
- ✅ Returns JSON responses instead of forwarding to JSP

**API Endpoints:**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/auth/signup` | Create new user | `{name, email, password, role}` | User object + success message |
| POST | `/api/auth/login` | Authenticate user | `{email, password}` | User object + session |
| POST | `/api/auth/logout` | End user session | - | Success message |
| GET | `/api/auth/current` | Get logged-in user | - | User object |

**Key Features:**
- Manual JSON parsing (no external libraries needed)
- Proper error responses with status codes
- Password excluded from responses
- Session-based authentication

---

### 7. CORS Filter (`CorsFilter.java`)

Located at: `src/main/java/com/enit/filters/CorsFilter.java`

**New File - Created to handle Cross-Origin Resource Sharing**

```java
@WebFilter("/api/*")
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Allow React dev server
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        // Handle preflight
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }
}
```

**What it does:**
- Allows frontend (localhost:5173) to make API calls to backend (localhost:8080)
- Handles preflight OPTIONS requests
- Enables credential sharing (cookies/sessions)

---

### 8. Web Configuration (`web.xml`)

Located at: `src/main/webapp/WEB-INF/web.xml`

**Changes Made:**
- ✅ Simplified configuration (servlet mapping uses `@WebServlet` annotation)
- ✅ Removed JSP-specific welcome files

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app id="WebApp_ID" version="2.4">
    <display-name>Convenia</display-name>
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```

---

## ⚛️ Frontend Configuration

### 1. Vite Configuration (`vite.config.js`)

Located at: `frontend/vite.config.js`

**Changes Made:**
- ✅ Added proxy configuration to forward `/api` requests to backend

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // Backend server
        changeOrigin: true,
        secure: false,
      }
    }
  }
})
```

**What it does:**
- When frontend makes request to `/api/auth/login`
- Vite proxies it to `http://localhost:8080/api/auth/login`
- Avoids CORS issues during development

---

### 2. Main Application (`App.jsx`)

Located at: `frontend/src/App.jsx`

**Changes Made:**
- ✅ Fixed `fetch()` syntax (removed template literal error)
- ✅ Added proper error handling
- ✅ Added form validation
- ✅ Implemented session credential support

**Key Features:**
```javascript
const handleSubmit = async () => {
  const endpoint = isLogin ? '/api/auth/login' : '/api/auth/signup';
  
  const response = await fetch(endpoint, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',  // Important for session cookies
    body: JSON.stringify(payload)
  });
  
  const data = await response.json();
  
  if (response.ok && data.success) {
    // Handle success
    console.log('User data:', data.user);
  }
};
```

---

### 3. Login Component (`Login.jsx`)

Located at: `frontend/src/components/Login.jsx`

**No Changes Required** - Component properly structured to work with the API.

**Fields:**
- Email address
- Password
- Remember me checkbox
- Forgot password link

---

### 4. Signup Component (`Signup.jsx`)

Located at: `frontend/src/components/Signup.jsx`

**No Changes Required** - Component properly structured to work with the API.

**Fields:**
- Full Name
- Email address
- Password
- Role (dropdown: ENSEIGNANT_CHERCHEUR, DRI, CA, CEVU, DEVE)
- Terms agreement checkbox

---

## 🔌 API Endpoints

### 1. User Signup

**Endpoint:** `POST /api/auth/signup`

**Request:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "ENSEIGNANT_CHERCHEUR"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "user": {
    "id": 1,
    "username": "john.doe",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "ENSEIGNANT_CHERCHEUR",
    "active": true
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email already exists"
}
```

---

### 2. User Login

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "username": "john.doe",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "ENSEIGNANT_CHERCHEUR",
    "active": true
  }
}
```

**Notes:**
- Creates a server-side session
- Session cookie automatically sent with response
- Password is never included in response

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

---

### 3. User Logout

**Endpoint:** `POST /api/auth/logout`

**Request:** No body required

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

**Notes:**
- Invalidates server-side session
- No authentication required

---

### 4. Get Current User

**Endpoint:** `GET /api/auth/current`

**Request:** No body required (uses session cookie)

**Success Response (200 OK):**
```json
{
  "success": true,
  "user": {
    "id": 1,
    "username": "john.doe",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "ENSEIGNANT_CHERCHEUR",
    "active": true
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Not authenticated"
}
```

---

## 🚀 Setup Instructions

### For First-Time Setup:

#### 1. Database Setup

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS conveniadb;

-- Verify creation
SHOW DATABASES;

-- The 'users' table will be auto-created by Hibernate on first deployment
```

#### 2. Update Database Credentials

Edit `src/main/webapp/WEB-INF/convenia-ds.xml`:
```xml
<user-name>your_mysql_username</user-name>
<password>your_mysql_password</password>
```

#### 3. Backend Setup

```bash
# 1. Import project into your IDE (Eclipse/IntelliJ)

# 2. Configure WildFly/JBoss server in your IDE

# 3. Deploy the project to the server
#    - Eclipse: Right-click project → Run As → Run on Server
#    - IntelliJ: Run configuration → JBoss/WildFly

# 4. Verify deployment in console
#    Look for: "Deployed Convenia.war"
```

#### 4. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend should start on http://localhost:5173
```

---

## 🧪 Testing the Integration

### Test Sequence:

#### 1. **Test Backend is Running**

Open browser: `http://localhost:8080/Convenia/`

You should see the welcome page (or 404 is fine if no index page exists).

#### 2. **Test API Endpoint Directly**

Use Postman or curl:

```bash
# Test signup
curl -X POST http://localhost:8080/Convenia/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123",
    "role": "ENSEIGNANT_CHERCHEUR"
  }'
```

Expected response:
```json
{"success":true,"message":"User created successfully","user":{...}}
```

#### 3. **Test Frontend**

1. Open `http://localhost:5173`
2. Click "Sign Up"
3. Fill the form:
   - Name: `Test User`
   - Email: `test@example.com`
   - Password: `test123`
   - Role: Select any
   - Check "agree to terms"
4. Click "Sign up"
5. Should see alert: "Signup successful!"

6. Click "Sign In"
7. Enter credentials:
   - Email: `test@example.com`
   - Password: `test123`
8. Click "Login"
9. Should see alert: "Login successful! Welcome Test User!"

#### 4. **Verify in Database**

```sql
USE conveniadb;
SELECT * FROM users;
```

Should see your test user.

#### 5. **Check Browser Developer Tools**

Press F12 → Network tab:
- You should see requests to `/api/auth/signup` and `/api/auth/login`
- Status codes should be 201 and 200
- Response should contain user data

Console tab:
- Should show: `User data: {id: 1, username: "test", ...}`

---

## 🐛 Troubleshooting

### Common Issues and Solutions:

#### 1. **CORS Error in Browser Console**

**Error:**
```
Access to fetch at 'http://localhost:8080/...' from origin 'http://localhost:5173' 
has been blocked by CORS policy
```

**Solution:**
- Verify `CorsFilter.java` exists and is compiled
- Check WildFly console for filter registration
- Restart both servers

---

#### 2. **404 Not Found**

**Error:** API endpoint returns 404

**Solution:**
- Check servlet URL mapping in `UserServlet.java`: `@WebServlet("/api/auth/*")`
- Verify WildFly deployment: Look for "Deployed Convenia.war" in console
- Check the complete URL includes context path: `http://localhost:8080/Convenia/api/auth/login`

---

#### 3. **500 Internal Server Error**

**Error:** Server returns 500

**Solution:**
- Check WildFly console for stack trace
- Common causes:
  - Database connection failed (check credentials)
  - Persistence unit not found (check persistence.xml)
  - EJB injection failed (check @EJB annotation)
  
---

#### 4. **Database Connection Failed**

**Error:**
```
Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
```

**Solution:**
```bash
# 1. Verify MySQL is running
sudo systemctl status mysql

# 2. Test connection
mysql -u root -p conveniadb

# 3. Check convenia-ds.xml credentials

# 4. Ensure MySQL JDBC driver is deployed in WildFly
# Location: WILDFLY_HOME/standalone/deployments/mysql-connector-java-8.x.x.jar
```

---

#### 5. **Persistence Unit Not Found**

**Error:**
```
No Persistence provider for EntityManager named myPU
```

**Solution:**
- Verify `persistence.xml` is in `src/main/resources/META-INF/`
- Check persistence unit name matches: `myPU`
- Ensure datasource JNDI name is correct: `java:/conveniaDS`
- Rebuild and redeploy

---

#### 6. **Session Not Working**

**Error:** User gets logged out immediately

**Solution:**
- Ensure `credentials: 'include'` is in fetch options
- Check CORS filter allows credentials
- Verify `Access-Control-Allow-Credentials: true` header is set

---

#### 7. **Frontend Shows Blank Page**

**Solution:**
```bash
# Check terminal for errors
# Common causes:

# 1. Port already in use
# Kill process on port 5173
lsof -ti:5173 | xargs kill -9

# 2. Dependencies not installed
npm install

# 3. Syntax error in code
# Check terminal for error messages
```

---

#### 8. **Changes Not Reflecting**

**Backend:**
```bash
# Clean and rebuild
mvn clean install  # if using Maven
# Or rebuild in IDE

# Restart WildFly
```

**Frontend:**
```bash
# Stop server (Ctrl+C)
# Clear cache
rm -rf node_modules/.vite

# Restart
npm run dev
```

---

## 👥 Team Setup Guide

### For Team Members Cloning the Repository:

#### Step 1: Clone Repository
```bash
git clone <repository-url>
cd CONVENIA
```

#### Step 2: Database Setup
```sql
-- Create your local database
CREATE DATABASE conveniadb;
```

#### Step 3: Configure Database Credentials

Edit `src/main/webapp/WEB-INF/convenia-ds.xml`:
```xml
<user-name>YOUR_MYSQL_USERNAME</user-name>
<password>YOUR_MYSQL_PASSWORD</password>
```

**⚠️ Important:** This file contains credentials. Consider:
- Adding it to `.gitignore`
- Creating a template file (`convenia-ds.xml.template`)
- Using environment variables in production

#### Step 4: Import Backend Project

**Eclipse:**
1. File → Import → Existing Projects into Workspace
2. Select project root directory
3. Configure WildFly/JBoss server
4. Run on Server

**IntelliJ IDEA:**
1. File → Open → Select project root
2. Configure Application Server (WildFly/JBoss)
3. Run configuration

#### Step 5: Setup Frontend

```bash
cd frontend
npm install
npm run dev
```

#### Step 6: Verify Setup

1. Backend: `http://localhost:8080/Convenia/`
2. Frontend: `http://localhost:5173/`
3. Test signup and login

---

## 📝 Development Notes

### Code Quality

- All backend code uses Jakarta EE APIs (not javax)
- No external JSON libraries required (manual parsing)
- Session-based authentication (stateful)
- RESTful API design principles followed

### Security Considerations

⚠️ **Current Implementation:**
- Passwords stored in plain text
- No password strength validation
- Basic session management

✅ **Recommended for Production:**
- Use BCrypt for password hashing
- Implement password complexity rules
- Add rate limiting to prevent brute force
- Use HTTPS
- Implement JWT tokens for stateless auth
- Add CSRF protection
- Input validation and sanitization

### Performance

- Connection pooling configured in datasource
- JPA second-level cache not enabled (add if needed)
- No query optimization (add indexes if needed)

### Future Enhancements

- [ ] Password reset functionality
- [ ] Email verification
- [ ] Role-based access control (RBAC)
- [ ] User profile management
- [ ] Remember me functionality
- [ ] OAuth2/LDAP integration
- [ ] API rate limiting
- [ ] Request logging
- [ ] Unit and integration tests

---

## 📞 Support

If you encounter issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review WildFly console logs
3. Check browser Developer Tools (F12)
4. Verify all configuration files are correct
5. Ensure all dependencies are installed

---

## 🔄 Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0.0 | 2024-12-XX | Initial integration - Backend REST API + React Frontend | Team |

---

## 📄 License

[Add your license here]

---

## 🤝 Contributors

[Add team members here]

---

**Last Updated:** December 2024