import React from "react";
import NavBar from "./NavBar";
import HeroIcon from "./SVGs/heroIcon";
import { Link } from "react-router-dom";
import NewTab from "./SVGs/NewTab";
import GithubIcon from "./SVGs/GithubIcon";

const HomeScreen = () => {
  return (
    <>
      <NavBar />
      <div className="hero-container">
        <div className="text-container">
          <div className="header">Welcome to Tic Tac Toe</div>
          <p className="info">
            The game has been developed by the talented developers at
            CodePhusion during their free time. The primary goal of this project
            is to learn the basics of Artificial Intelligence (AI) by
            implementing algorithms Minimax and Minimax with Alpha-Beta Pruning.
          </p>

          <div className="btn-container">
            <Link to="/StartScreen" className="btn-text hero-btn blue-btn">
              Start Game
            </Link>
            <a
              href="https://www.codephusion.com/"
              target="_blank"
              rel="noopener noreferrer"
              className="btn-text hero-btn yellow-btn"
            >
              Visit Company Website{" "}
              <NewTab className="btn-logo" color={"#252835"} />
            </a>
          </div>
          <div className="btn-container">
            <a
              href="https://github.com/code-phusion/tic-tac-toe"
              target="_blank"
              rel="noopener noreferrer"
              className="btn-text hero-btn pink-btn"
            >
              See Github Project <GithubIcon className="btn-logo" />
            </a>
          </div>
        </div>
        <HeroIcon className="hero-icon" />
      </div>
    </>
  );
};

export default HomeScreen;
