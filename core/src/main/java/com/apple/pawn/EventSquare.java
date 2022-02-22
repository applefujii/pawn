package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

public class EventSquare extends Square {
    private final int move;
    public EventSquare(int xPos, int yPos, int ty, TextureAtlas atlas) {
        super(xPos, yPos, ty, atlas);
        move = MathUtils.random(1, 6);
    }

    public void update() {

    }
}
