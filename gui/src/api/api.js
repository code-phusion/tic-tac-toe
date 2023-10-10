import apiClient from "./axios";

export async function apiGet(path, params) {
  const response = await apiClient.get(path, {params});
  return response?.data;
}

export async function apiPost(path, body) {
  const response = await apiClient.post(path, body);
  return response?.data;
}
