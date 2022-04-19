package com.apple.pawn;

import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fujii
 */
public class SaveData {

    //-- game state
    @JsonProperty
    public float timer;
    @JsonProperty
    public int mapNo;
    @JsonProperty
    public int goalNo;
    @JsonProperty
    public int sequenceNo;					// シークエンス番号
    @JsonProperty
    public int turnPlayerNo;				// 何人目のプレイヤーのターンか
    //-- player state
    @JsonProperty
    public Array<Player> aPlayer;

    public SaveData() {
    }

    public SaveData(SaveData sd) {
        timer = sd.timer;
        mapNo = sd.mapNo;
        goalNo = sd.goalNo;
        sequenceNo = sd.sequenceNo;
        turnPlayerNo = sd.turnPlayerNo;
        aPlayer = new Array<>(sd.aPlayer);
    }

    public SaveData(float timer, int goalNo, int sequenceNo, int turnPlayerNo, Array<Player> aPlayer) {
        this.timer = timer;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
        this.aPlayer = aPlayer;
    }

    public void setGameState(float timer, int mapNo, int goalNo, int sequenceNo, int turnPlayerNo) {
        this.timer = timer;
        this.mapNo = mapNo;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
    }

    public SaveData cpy() {
        return new SaveData(this);
    }
}
