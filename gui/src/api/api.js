import apiClient from "./axios";

const gameApi = {
  newGame: async (args) => {
    const response = await apiClient.post(`/game/new?size=${args.fieldSize}&againstAI=${args.againstAI}&winNumber=${args.winNumber}&aiId=${args.aiId}`);
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

  move: async (gameId, row, col) => {
    const response = await apiClient.post(`/game/${gameId}/move?row=${row}&col=${col}`);
    return response?.data;
  },

  makeAIMove: async (gameId) => {
    const response = await apiClient.post(`/ai/${gameId}/move`);
    return response?.data;
  },

  getAIList: async () => {
    const response = await apiClient.get(`/ai`);
    return response?.data;
  },
}

export default gameApi;