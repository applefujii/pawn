package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square {
    protected int type;               // マスの種類
    protected String document;        // マスの文字

    protected Sprite normal;

    protected int x;
    protected int y;

    public Square(int xPos, int yPos) {
        x = xPos;
        y = yPos;
        normal = BoardSurface.mapAtlas.createSprite("normal");
        normal.flip(false, true);
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        normal.setSize(BoardSurface.TILE_LENGTH, BoardSurface.TILE_LENGTH);
        normal.setPosition(x*BoardSurface.TILE_LENGTH, y*BoardSurface.TILE_LENGTH);
        normal.draw(batch);
        batch.end();
    }

    public void dispose () {
    }
}
