import * as React from "react";
const ClearBoardIcon = props => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={40}
    height={40}
    fill="none"
    {...props}
  >
    <rect width={39} height={39} x={0.5} y={0.5} stroke="#F859A5" rx={7.5} />
    <path
      fill="#F859A5"
      d="M27 13.75a.75.75 0 0 0 0-1.5v1.5Zm-14-1.5a.75.75 0 0 0 0 1.5v-1.5Zm5.75 4.25a.75.75 0 0 0-1.5 0h1.5Zm-1.5 8a.75.75 0 0 0 1.5 0h-1.5Zm4 0a.75.75 0 0 0 1.5 0h-1.5Zm1.5-8a.75.75 0 0 0-1.5 0h1.5Zm2.5-3.5v13h1.5V13h-1.5Zm0 13c0 .686-.564 1.25-1.25 1.25v1.5A2.756 2.756 0 0 0 26.75 26h-1.5ZM24 27.25h-8v1.5h8v-1.5Zm-8 0c-.686 0-1.25-.564-1.25-1.25h-1.5A2.756 2.756 0 0 0 16 28.75v-1.5ZM14.75 26V13h-1.5v13h1.5Zm4-1.5v-8h-1.5v8h1.5ZM13 13.75h14v-1.5H13v1.5Zm9.75 10.75v-8h-1.5v8h1.5Z"
    />
    <path
      stroke="#F859A5"
      strokeLinecap="round"
      strokeWidth={1.5}
      d="M17 12h6"
    />
  </svg>
);
export default ClearBoardIcon;
