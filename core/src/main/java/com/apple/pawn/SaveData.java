package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * セーブデータ
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
    @JsonProperty
    public int turnCount;                   // 何ターン目か
    //-- player state
    @JsonProperty
    public Array<Player> aPlayer;

    public SaveData() {
    }

    public SaveData(@NonNull SaveData sd) {
        timer = sd.timer;
        mapNo = sd.mapNo;
        goalNo = sd.goalNo;
        sequenceNo = sd.sequenceNo;
        turnPlayerNo = sd.turnPlayerNo;
        turnCount = sd.turnCount;
        aPlayer = new Array<>(sd.aPlayer);
    }

    public SaveData(float timer, int goalNo, int sequenceNo, int turnPlayerNo, int turnCount, Array<Player> aPlayer) {
        this.timer = timer;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
        this.turnCount = turnCount;
        this.aPlayer = aPlayer;
    }

    public void setGameState(float timer, int mapNo, int goalNo, int sequenceNo, int turnPlayerNo, int turnCount) {
        this.timer = timer;
        this.mapNo = mapNo;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
        this.turnCount = turnCount;
    }

    public SaveData cpy() {
        return new SaveData(this);
    }
}
