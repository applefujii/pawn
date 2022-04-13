package com.apple.pawn;

import com.badlogic.gdx.Gdx;

import java.util.stream.IntStream;

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
        for(int i : IntStream.range(0,playerNo).toArray()) {
            aName[i] = (i+1)+"P";
            aColorNo[i] = i;
            aSquareNo[i] = 0;
        }
    }

    public void setAName(int no, String name) {
        aName[no] = name;
    }

    public void setAColorNo(int no, int colorNo) {
        aColorNo[no] = colorNo;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public String[] getAName() {
        //Gdx.app.debug("fps", "aName="+aName);
        return aName;
    }

    public int[] getAColorNo() {
        return aColorNo;
    }

    public int[] getASquareNo() {
        return aSquareNo;
    }

}
