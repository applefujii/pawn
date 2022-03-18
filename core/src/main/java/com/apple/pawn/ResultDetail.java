package com.apple.pawn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDetail {
    @JsonIgnore
    private Square square;
    @JsonProperty
    private int turn;
    @JsonProperty
    private boolean taskResult;

    public ResultDetail() {
    }

    public ResultDetail(Square square, int turn) {
        this.square = square;
        this.turn = turn;
        taskResult = true;
    }

    public void setTaskResult(boolean taskResult) {
        this.taskResult = taskResult;
    }

    public Square getSquare() {
        return square;
    }

    public int getTurn() {
        return turn;
    }

    public String getTaskResult() {
        if(square.getType() == 4) {
            if(taskResult) return "成功";
            else return "失敗";
        }
        else return "";
    }
}
