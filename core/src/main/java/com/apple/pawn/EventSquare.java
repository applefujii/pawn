package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;

public class EventSquare extends Square {
    private int move;

    public EventSquare(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
        super(pos, type, count, document, atlas);
        move = MathUtils.random(1, 6);
        if(move > BoardSurface.SQUARE_COUNT - this.count) move = BoardSurface.SQUARE_COUNT - this.count;
    }

    @Override
    public void update() { }
}
