import React from "react";
import "./App.css";
import {
  BrowserRouter as Router,
  // Navigate,
  Route,
  Routes,
} from "react-router-dom";
// import StartScreen from "./StartScreen";
import TicTacToe from "./components/TicTacToe";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import HomeScreen from "./components/HomeScreen";
import StartScreen from "./components/StartScreen";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
      cacheTime: 0,
    },
  },
});

function App() {
  return (
    <div className="App">
      <QueryClientProvider client={queryClient}>
        <Router>
          <Routes>
            <Route exact path="/" element={<HomeScreen />} />
            <Route path="StartScreen" element={<StartScreen />} />
            <Route path="/game/:gameId" element={<TicTacToe />} />
            {/* <Route path="*" element={<Navigate replace to="/" />} /> */}
          </Routes>
        </Router>
      </QueryClientProvider>
    </div>
  );
}

export default App;
