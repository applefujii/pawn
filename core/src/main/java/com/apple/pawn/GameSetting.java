package com.apple.pawn;

import java.util.Objects;
import java.util.stream.IntStream;

public class GameSetting {

    private int playerNo;
    private String[] aName;
    private int[] aColorNo;
    private int[] aSquareNo;
    private int stageNo;

    public GameSetting() { }

    public void init(int playerNo) {
        this.playerNo = playerNo;
        aName = new String[playerNo];
        aColorNo = new int[playerNo];
        aSquareNo = new int[playerNo];
        for (int i : IntStream.range(0, playerNo).toArray()) {
            aName[i] = (i + 1) + "P";
            aColorNo[i] = i;
            aSquareNo[i] = 0;
        }
        stageNo = 0;
    }

    public void setAName(int no, String name) {
        if(Objects.nonNull(name)) {
            String na = name.trim();
            if(!na.isEmpty()) {
                aName[no] = name;
            }
        }
    }

    public void setAColorNo(int no, int colorNo) {
        aColorNo[no] = colorNo;
    }

    public void setStageNo(int stageNo) {
        this.stageNo = stageNo;
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

    public int getStageNo() {
        return stageNo;
    }
}
