package com.upskill.tictactoe.dto;

import com.upskill.tictactoe.model.AIModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIListResponse {
  private List<AIModel> list;
}
