import apiClient from "./axios";

const gameApi = {
  newGame: async (size) => {
    const response = await apiClient.post(`/game/new?size=${size}`);
    return response?.data;
  },

  getState: async () => {
    const response = await apiClient.get(`/game/state`);
    return response?.data;
  },

  move: async (row, col) => {
    const response = await apiClient.post(`/game/move?row=${row}&col=${col}`);
    return response?.data;
  }
}

export default gameApi;
