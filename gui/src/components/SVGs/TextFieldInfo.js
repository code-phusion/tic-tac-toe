import * as React from "react";
const TextFieldInfo = props => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={24}
    height={24}
    fill="none"
    {...props}
  >
    <path
      stroke="#A2A9BF"
      strokeLinecap="round"
      strokeWidth={1.5}
      d="M12 7v6m9-1a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
    />
    <circle cx={12} cy={16} r={1} fill="#A2A9BF" />
  </svg>
);
export default TextFieldInfo;
