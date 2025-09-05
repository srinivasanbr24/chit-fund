import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8082", // your Spring Boot backend URL
});

// Add JWT token automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
