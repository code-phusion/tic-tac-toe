import * as React from "react";
const NewTab = props => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={20}
    height={20}
    fill="none"
    {...props}
  >
    <path
      fill={props?.color ? props?.color : "#F9F9FC"}
      d="M17.417 3.333a.75.75 0 0 0-.75-.75h-6.75a.75.75 0 1 0 0 1.5h6v6a.75.75 0 1 0 1.5 0v-6.75ZM6.97 11.97a.75.75 0 1 0 1.06 1.06l-1.06-1.06Zm9.166-9.167L6.97 11.97l1.06 1.06 9.167-9.166-1.06-1.061Z"
    />
    <path
      fill={props?.color ? props?.color : "#F9F9FC"}
      fillRule="evenodd"
      d="M6.667 2.5H4.5a2 2 0 0 0-2 2v11a2 2 0 0 0 2 2h11a2 2 0 0 0 2-2v-2.167H16V15.5a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5h2.167V2.5Z"
      clipRule="evenodd"
    />
  </svg>
);
export default NewTab;
