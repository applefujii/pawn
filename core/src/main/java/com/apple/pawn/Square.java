package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Square {
    protected static Array<String> TYPE_STR;

    protected int type;               // マスの種類
    protected String document;        // マスの文字

    protected TextureAtlas mapAtlas;
    protected Sprite sprite;

    protected int x;
    protected int y;

    static {
        TYPE_STR = new Array<>();
        TYPE_STR.add("start", "goal", "normal", "event");
    }

    public Square(int xPos, int yPos, int ty, TextureAtlas atlas) {
        x = xPos;
        y = yPos;
        type = ty;
        mapAtlas = atlas;
        sprite = mapAtlas.createSprite(TYPE_STR.get(type));
        sprite.flip(false, true);
    }

    public void update() { }

    public void draw (Batch batch) {
        sprite.setSize(BoardSurface.TILE_LENGTH, BoardSurface.TILE_LENGTH);
        sprite.setPosition(x*BoardSurface.TILE_LENGTH, y*BoardSurface.TILE_LENGTH);
        sprite.draw(batch);
    }

    public void dispose () { }
}
