import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Button,
  FormControl,
  FormControlLabel,
  MenuItem,
  Radio,
  RadioGroup,
  TextField,
  Tooltip,
  Typography,
} from "@mui/material";
import gameApi from "../api/api";
import NavBar from "./NavBar";
import AiIcon from "./SVGs/AiIcon";
import HumanIcon from "./SVGs/HumanIcon";
import DropDownIcon from "./SVGs/DropDownIcon";
import TextFieldInfo from "./SVGs/TextFieldInfo";

function StartScreen() {
  const [size, setSize] = useState(3);
  const [winNumber, setWinNumber] = useState(3);
  const [aiList, setAiList] = useState([]);
  const [selectedAiModel, setSelectedAiModel] = useState({ id: "" });
  const navigate = useNavigate();

  const [openDropDown, setOpenDropDown] = useState(false);

  const [warning, setWarning] = useState("");

  useEffect(() => {
    async function fetchData() {
      const response = await gameApi.getAIList();
      setAiList(response?.list);
      if (response.list && response.list.length) {
        setSelectedAiModel(response.list[0]);
      }
    }
    fetchData();
  }, []);

  const startGame = async againstAI => {
    try {
      const fieldSize = parseInt(size, 10);
      if (againstAI) {
        if (
          fieldSize > selectedAiModel.maxBoardSize ||
          winNumber !== selectedAiModel.winNumber
        ) {
          setWarning(
            "Sorry, Selected AI only supports a Field Size <= " +
              selectedAiModel.maxBoardSize +
              " and a Win Number = " +
              selectedAiModel.winNumber
          );
          return;
        }
      }

      if (fieldSize < 3 || fieldSize > 10) {
        // alert("Field Size should be between 3 and 10");
        return;
      }

      if (winNumber < 3 || winNumber > fieldSize) {
        // alert("Win Number should be between 3 and the Field Size");
        return;
      }

      setWarning("");

      const response = await gameApi.newGame({
        fieldSize,
        winNumber,
        againstAI,
        aiId: selectedAiModel.id,
      });

      const gameId = response?.gameId;
      navigate(`/game/${gameId}`);
    } catch (error) {
      console.error("An error occurred while starting the game: ", error);
    }
  };

  const aiListOptions = [];
  aiList?.forEach(aiModel => {
    aiListOptions.push(
      <MenuItem key={aiModel?.id} value={aiModel?.id}>
        {aiModel?.name}
      </MenuItem>
    );
  });

  return (
    <>
      <NavBar />
      <div className="form-container">
        <div className="form">
          <p className="form-header">Choose preferred settings</p>
          <TextField
            type="number"
            variant="standard"
            label="Enter Field Size"
            fullWidth
            value={size.toString()}
            onChange={e => setSize(Number(e.target.value))}
            style={{ marginBottom: "20px" }}
            className="textField"
            sx={{
              "input::-webkit-outer-spin-button, input::-webkit-inner-spin-button":
                {
                  WebkitAppearance: "none",
                  margin: 0,
                },
              "input[type=number]": {
                MozAppearance: "textfield",
              },
            }}
            error={size < 3 || size > 10}
            helperText={
              size < 3 || size > 10
                ? "Field Size should be between 3 and 10"
                : ""
            }
          />
          <TextField
            type="number"
            variant="standard"
            label="Enter Win Number (how many cells to fill in row to win)"
            fullWidth
            value={winNumber.toString()}
            onChange={e => setWinNumber(Number(e.target.value))}
            style={{ marginBottom: "20px" }}
            className="textField numberToWin"
            sx={{
              "input::-webkit-outer-spin-button, input::-webkit-inner-spin-button":
                {
                  WebkitAppearance: "none",
                  margin: 0,
                },
              "input[type=number]": {
                MozAppearance: "textfield",
              },
            }}
            error={winNumber < 3 || winNumber > size}
            helperText={
              winNumber < 3 || winNumber > size
                ? "Win Number should be between 3 and the Field Size"
                : ""
            }
          />
          <div className="numberToWinMobile">
            <TextField
              type="number"
              variant="standard"
              label="Enter Win Number"
              fullWidth
              value={winNumber.toString()}
              onChange={e => setWinNumber(Number(e.target.value))}
              style={{ marginBottom: "20px" }}
              className="textField "
              sx={{
                "input::-webkit-outer-spin-button, input::-webkit-inner-spin-button":
                  {
                    WebkitAppearance: "none",
                    margin: 0,
                  },
                "input[type=number]": {
                  MozAppearance: "textfield",
                },
              }}
              error={winNumber < 3 || winNumber > size}
              helperText={
                winNumber < 3 || winNumber > size
                  ? "Win Number should be between 3 and the Field Size"
                  : ""
              }
            />
            <Tooltip
              disableFocusListener
              title="how many cells to fill in a row to win"
              arrow
              enterTouchDelay={0}
              componentsProps={{
                tooltip: {
                  sx: {
                    bgcolor: "#252835D9",
                    borderRadius: "4px",
                    color: "#F9F9FC",
                    py: 1,
                    px: 2,
                    fontFamily: "Kanit",
                    fontSize: "12px",
                    lineHeight: "16px",
                    maxWidth: "100%",
                    fontWeight: "400",
                  },
                },
                arrow: {
                  "&::before": {
                    backgroundColor: "F9F9FC",
                  },
                },
              }}
            >
              <Button className="textFieldInfo">
                <TextFieldInfo onClick={() => console.log("ehher")} />
              </Button>
            </Tooltip>
          </div>
          <div
            onClick={() => setOpenDropDown(!openDropDown)}
            className={`form-dropdown ${
              openDropDown ? "margin-bottom-sm" : "margin-bottom"
            }`}
          >
            Select AI algo{" "}
            <DropDownIcon
              className={`arrow-icon ${
                openDropDown ? "arrow-icon-rotate" : ""
              }`}
            />
          </div>
          <div
            style={{
              animation: `${
                openDropDown ? "slide-up" : "slide-down"
              } 0.5s ease forwards`,
              overflow: "hidden",
            }}
          >
            <FormControl>
              <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                name="radio-buttons-group"
                value={selectedAiModel?.id || ""}
                onChange={e => {
                  const aiModel = aiList.find(curAiModel => {
                    return curAiModel.id === e.target.value;
                  });
                  setSelectedAiModel(aiModel);
                }}
                className={`${openDropDown ? "margin-bottom" : ""}`}
                sx={{
                  gap: "8px",
                  mt: 2,
                  ".MuiButtonBase-root": {
                    color: "#5F77F4",
                  },
                  ".Mui-checked ": {
                    color: "#5F77F4 !important",
                  },
                }}
              >
                {aiListOptions?.map(data => (
                  <FormControlLabel
                    key={data?.key}
                    value={data?.props?.value}
                    control={<Radio />}
                    label={data?.props?.children}
                    className="radio-label"
                  />
                ))}
              </RadioGroup>
            </FormControl>
          </div>
          <Typography
            variant="caption"
            display="block"
            color="error"
            sx={{
              textAlign: "center",
              margin: "-12px 0 12px 0",
            }}
          >
            {warning && warning}
          </Typography>
          <div className="btn-container">
            <div onClick={() => startGame(true)} className="btn-form blue-btn">
              <AiIcon /> Start Against AI
            </div>
            <div
              onClick={() => startGame(false)}
              className="btn-form yellow-btn"
            >
              <HumanIcon /> Start Against Human
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default StartScreen;
