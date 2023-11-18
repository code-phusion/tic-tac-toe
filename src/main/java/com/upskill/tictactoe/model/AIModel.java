package com.upskill.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIModel {
    private String id;
    private String name;
    private int maxBoardSize;
    private int winNumber;
}
