import axios from 'axios';

const baseUrl = process.env.REACT_APP_API_BASE_URL;

const apiClient = axios.create({
  baseURL: baseUrl
});

export default apiClient;
