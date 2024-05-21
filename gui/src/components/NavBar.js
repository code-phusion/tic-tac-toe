import React from "react";
import Logo from "./SVGs/Logo";
import NewTab from "./SVGs/NewTab";
import { useNavigate } from "react-router-dom";

const NavBar = () => {
  const navigate = useNavigate();
  return (
    <div className="navbar">
      <Logo onClick={() => navigate(`/`)} />
      <a
        href="https://www.codephusion.com/"
        target="_blank"
        rel="noopener noreferrer"
        className="text-des nav-btn"
      >
        <span className="nav-btn-text">Visit Company Website</span>
        <NewTab />
      </a>
    </div>
  );
};

export default NavBar;
