import * as React from "react";
const BackIcon = props => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={40}
    height={40}
    fill="none"
    {...props}
  >
    <rect width={39} height={39} x={0.5} y={0.5} stroke="#252835" rx={7.5} />
    <path
      stroke="#252835"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={1.5}
      d="m23 15-6 5 6 5"
    />
  </svg>
);
export default BackIcon;
