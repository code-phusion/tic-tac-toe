import * as React from "react";
const AiIcon = props => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={24}
    height={24}
    fill="none"
    {...props}
  >
    <path
      stroke="#fff"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={1.5}
      d="M2.625 8.625h18.75v11.25H2.625V8.625Z"
    />
    <path
      stroke="#fff"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={1.5}
      d="M16.125 13.125v2.25m-8.25-2.25v2.25m-.75-11.25L12 8.625l4.875-4.5"
    />
  </svg>
);
export default AiIcon;
