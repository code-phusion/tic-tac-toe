package com.upskill.tictactoe.service.ai;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

public interface AIInterface {
    Move calculateMove(TicTacToeBoardModel board, char aiSymbol);

    int getMaxBoardSize();

    int getWinNumber();

    String getId();

    String getName();
}
