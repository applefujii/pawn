package com.apple.pawn;

import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fujii
 */
public class SaveData {

    private Array<Player> player;

    public void setPlayer(Array<Player> player) {
        this.player = player;
    }
}
