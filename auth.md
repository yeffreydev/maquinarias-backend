# Authentication API Documentation

## Overview
This API provides JWT-based authentication for the Maquinarias application. It supports user registration and login with email/password credentials.

## Base URL
```
http://localhost:8080/api/auth
```

## Endpoints

### 1. User Registration
**POST** `/api/auth/register`

Register a new user account.

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "password123",
  "role": "USER",
  "fullname": "John Doe"
}
```

#### Response (Success - 200 OK)
```json
"User registered successfully!"
```

#### Response (Error - 400 Bad Request)
```json
"Email is already in use!"
```

### 2. User Login
**POST** `/api/auth/login`

Authenticate user and receive JWT token.

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Response (Success - 200 OK)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful"
}
```

#### Response (Error - 400 Bad Request)
```json
"Invalid email or password"
```

## Authentication Flow

### Frontend Integration

#### 1. Registration
```javascript
// Example using fetch
const registerUser = async (userData) => {
  try {
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData)
    });

    if (response.ok) {
      const result = await response.text();
      console.log(result); // "User registered successfully!"
    } else {
      const error = await response.text();
      console.error(error);
    }
  } catch (error) {
    console.error('Registration failed:', error);
  }
};

// Usage
registerUser({
  email: 'user@example.com',
  password: 'password123',
  role: 'USER',
  fullname: 'John Doe'
});
```

#### 2. Login
```javascript
// Example using fetch
const loginUser = async (credentials) => {
  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials)
    });

    if (response.ok) {
      const data = await response.json();
      // Store the JWT token
      localStorage.setItem('token', data.token);
      console.log(data.message); // "Login successful"
    } else {
      const error = await response.text();
      console.error(error);
    }
  } catch (error) {
    console.error('Login failed:', error);
  }
};

// Usage
loginUser({
  email: 'user@example.com',
  password: 'password123'
});
```

#### 3. Making Authenticated Requests
```javascript
// Example authenticated request
const makeAuthenticatedRequest = async (url, options = {}) => {
  const token = localStorage.getItem('token');

  if (!token) {
    throw new Error('No authentication token found');
  }

  const headers = {
    ...options.headers,
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json',
  };

  try {
    const response = await fetch(url, {
      ...options,
      headers
    });

    if (response.status === 401) {
      // Token expired or invalid, redirect to login
      localStorage.removeItem('token');
      window.location.href = '/login';
      return;
    }

    return response;
  } catch (error) {
    console.error('Request failed:', error);
    throw error;
  }
};

// Usage
const getProtectedData = async () => {
  try {
    const response = await makeAuthenticatedRequest('/api/protected-endpoint');
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.error('Failed to fetch protected data:', error);
  }
};
```

#### 4. Logout
```javascript
const logout = () => {
  localStorage.removeItem('token');
  // Redirect to login page
  window.location.href = '/login';
};
```

## Security Features

- **Password Encryption**: Passwords are hashed using BCrypt
- **JWT Tokens**: Stateless authentication with expiration
- **CORS Support**: Configured for cross-origin requests
- **CSRF Protection**: Disabled for API endpoints
- **Session Management**: Stateless (no server-side sessions)

## Token Expiration

- JWT tokens expire after 24 hours
- Include the token in the `Authorization` header as `Bearer {token}`
- Handle 401 responses by redirecting to login

## Error Handling

- 400 Bad Request: Invalid input or credentials
- 401 Unauthorized: Invalid or expired token
- 500 Internal Server Error: Server-side issues

## Testing with cURL

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "role": "USER",
    "fullname": "Test User"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Access Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"