import React from 'react';

import './App.css';

import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';

import StartScreen from "./components/StartScreen";
import TicTacToe from "./components/TicTacToe";

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route exact path="/" element={<StartScreen />} />
          <Route path="/game" element={<TicTacToe />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;

