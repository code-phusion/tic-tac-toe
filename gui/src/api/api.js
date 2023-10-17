import apiClient from "./axios";

const gameApi = {
  newGame: async (size) => {
    const response = await apiClient.post(`/game/new?size=${size}`);
    return response?.data;
  },

  getState: async (gameId, stateId) => {
    const response = await apiClient.get(`/game/${gameId}/state/${stateId}`);
    return response?.data;
  },

  move: async (gameId, row, col) => {
    const response = await apiClient.post(`/game/${gameId}/move?row=${row}&col=${col}`);
    return response?.data;
  }
}

export default gameApi;
