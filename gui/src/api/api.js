import apiClient from "./axios";

const gameApi = {
  newGame: async (size, againstAI) => {
    const response = await apiClient.post(`/game/new?size=${size}&againstAI=${againstAI}`);
    return response?.data;
  },

  getState: async (gameId, stateId) => {
    const response = await apiClient.get(`/game/${gameId}/state/${stateId}`);
    return response?.data;
  },

  clearBoard: async (gameId) => {
    const response = await apiClient.post(`/game/${gameId}/clear-board`);
    return response?.data;
  },

  makeAIMove: async (gameId) => {
    const response = await apiClient.post(`/game/${gameId}/ai-move`);
    return response?.data;
  },

  move: async (gameId, row, col) => {
    const response = await apiClient.post(`/game/${gameId}/move?row=${row}&col=${col}`);
    return response?.data;
  }
}

export default gameApi;