package com.apple.pawn;

public class GameSetting {

    private int playerNo;
    private String[] aName;
    private int[] aColorNo;
    private int[] aSquareNo;

    public GameSetting() {
    }

    public void init(int playerNo) {
        this.playerNo = playerNo;
        aName = new String[playerNo];
        aColorNo = new int[playerNo];
        aSquareNo = new int[playerNo];
    }

    public void setAName(int no, String name) {
        aName[no] = name;
    }

    public void setAColorNo(int no, int colorNo) {
        aColorNo[no] = colorNo;
    }

    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public String[] getAName() {
        return aName;
    }

    public int[] getAColorNo() {
        return aColorNo;
    }
}
