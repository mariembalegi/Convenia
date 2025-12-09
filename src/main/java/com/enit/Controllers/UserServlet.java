package com.enit.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.enit.entities.User;
import com.enit.service.UserServiceLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/auth/*")
public class UserServlet extends HttpServlet {

    @EJB
    private UserServiceLocal userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if ("/login".equals(pathInfo)) {
                handleLogin(request, response);
            } else if ("/signup".equals(pathInfo)) {
                handleSignup(request, response);
            } else if ("/logout".equals(pathInfo)) {
                handleLogout(request, response);
            } else {
                sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if ("/current".equals(pathInfo)) {
                handleGetCurrentUser(request, response);
            } else {
                sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, 500, "Internal server error");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String jsonString = readRequestBody(request);
        
        // Manual JSON parsing
        String email = extractJsonValue(jsonString, "email");
        String password = extractJsonValue(jsonString, "password");

        if (email == null || password == null) {
            sendError(response, 400, "Email and password are required");
            return;
        }

        User user = userService.loginByEmail(email, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            String responseJson = buildSuccessResponse(
                "Login successful",
                buildUserJson(user)
            );

            sendJsonResponse(response, 200, responseJson);
        } else {
            sendError(response, 401, "Invalid email or password");
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String jsonString = readRequestBody(request);

        // Manual JSON parsing
        String name = extractJsonValue(jsonString, "name");
        String email = extractJsonValue(jsonString, "email");
        String password = extractJsonValue(jsonString, "password");
        String role = extractJsonValue(jsonString, "role");

        if (name == null || email == null || password == null || role == null) {
            sendError(response, 400, "All fields are required");
            return;
        }

        // Check if email exists
        if (userService.findByEmail(email) != null) {
            sendError(response, 400, "Email already exists");
            return;
        }

        // Create username from email (before @)
        String username = email.split("@")[0];

        // Check if username exists
        if (userService.findByUsername(username) != null) {
            username = username + System.currentTimeMillis();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(name);
        user.setRole(role);
        user.setActive(true);

        try {
            userService.create(user);

            String responseJson = buildSuccessResponse(
                "User created successfully",
                buildUserJson(user)
            );

            sendJsonResponse(response, 201, responseJson);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, 500, "User creation failed: " + e.getMessage());
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        String responseJson = "{\"success\":true,\"message\":\"Logged out successfully\"}";
        sendJsonResponse(response, 200, responseJson);
    }

    private void handleGetCurrentUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                String responseJson = buildSuccessResponse(null, buildUserJson(user));
                sendJsonResponse(response, 200, responseJson);
                return;
            }
        }

        sendError(response, 401, "Not authenticated");
    }

    // ========== HELPER METHODS ==========

    private String readRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Simple JSON value extractor
     * Extracts value for a given key from JSON string
     */
    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\"";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex = json.indexOf(":", startIndex) + 1;
            startIndex = json.indexOf("\"", startIndex) + 1;
            int endIndex = json.indexOf("\"", startIndex);

            if (endIndex == -1) return null;

            return json.substring(startIndex, endIndex);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Build JSON response for user object (without password)
     */
    private String buildUserJson(User user) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":").append(user.getId()).append(",");
        json.append("\"username\":\"").append(escapeJson(user.getUsername())).append("\",");
        json.append("\"email\":\"").append(escapeJson(user.getEmail())).append("\",");
        json.append("\"fullName\":\"").append(escapeJson(user.getFullName())).append("\",");
        json.append("\"role\":\"").append(escapeJson(user.getRole())).append("\",");
        json.append("\"active\":").append(user.isActive());
        json.append("}");
        return json.toString();
    }

    /**
     * Build success response with optional user data
     */
    private String buildSuccessResponse(String message, String userData) {
        StringBuilder json = new StringBuilder();
        json.append("{\"success\":true");
        
        if (message != null) {
            json.append(",\"message\":\"").append(escapeJson(message)).append("\"");
        }
        
        if (userData != null) {
            json.append(",\"user\":").append(userData);
        }
        
        json.append("}");
        return json.toString();
    }

    /**
     * Escape special characters in JSON strings
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Send JSON response
     */
    private void sendJsonResponse(HttpServletResponse response, int status, String json)
            throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Send error response
     */
    private void sendError(HttpServletResponse response, int status, String message)
            throws IOException {
        String errorJson = "{\"success\":false,\"message\":\"" + escapeJson(message) + "\"}";
        sendJsonResponse(response, status, errorJson);
    }
}