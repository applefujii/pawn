package com.apple.pawn;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author fujii
 */
public class SaveData {

    //-- game state
    @JsonProperty
    public float timer;
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

    public SaveData(float timer, int goalNo, int sequenceNo, int turnPlayerNo, Array<Player> aPlayer) {
        this.timer = timer;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
        this.aPlayer = aPlayer;
    }

    public void setGameState(float timer, int goalNo, int sequenceNo, int turnPlayerNo) {
        this.timer = timer;
        this.goalNo = goalNo;
        this.sequenceNo = sequenceNo;
        this.turnPlayerNo = turnPlayerNo;
    }

}
