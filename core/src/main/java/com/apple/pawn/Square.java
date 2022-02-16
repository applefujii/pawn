package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square {
    public static int SQUARE_LENGTH = 256;

    protected int type;               // マスの種類
    protected String document;        // マスの文字

    protected int x;
    protected int y;

    public Square(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 1, 1, 1);
        renderer.box(x*SQUARE_LENGTH+1, y*SQUARE_LENGTH+1, 0, SQUARE_LENGTH-2, SQUARE_LENGTH-2, 0);
        renderer.end();
    }

    public void dispose () {
    }
}
