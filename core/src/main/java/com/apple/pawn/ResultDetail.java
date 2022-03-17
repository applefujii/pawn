package com.apple.pawn;

public class ResultDetail {
    private final Square square;
    private final int turn;
    private boolean taskResult;

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
