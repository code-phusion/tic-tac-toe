import React from "react";
import Logo from "./SVGs/Logo";
import NewTab from "./SVGs/NewTab";

const NavBar = () => {
  return (
    <div className="navbar">
      <Logo />
      <a
        href="https://www.codephusion.com/"
        target="_blank"
        rel="noopener noreferrer"
        className="text-des nav-btn"
      >
        Visit Company Website <NewTab />
      </a>
    </div>
  );
};

export default NavBar;
