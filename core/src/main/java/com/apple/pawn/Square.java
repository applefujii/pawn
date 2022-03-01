package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Square {
    public static Array<String> TYPE_STR;

    protected final Sprite sprite;

    protected final int x;
    protected final int y;
    protected final int type;
    protected final int count;

    static {
        TYPE_STR = new Array<>();
        TYPE_STR.add("start", "goal", "normal", "event");
    }

    public Square(int x, int y, int type, int count, TextureAtlas atlas) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.count = count;
        // マスの種類
        sprite = atlas.createSprite(TYPE_STR.get(this.type));
        sprite.flip(false, true);
    }

    public void update() { }

    public void draw (Batch batch) {
        sprite.setSize(BoardSurface.TILE_LENGTH, BoardSurface.TILE_LENGTH);
        sprite.setPosition(x*BoardSurface.TILE_LENGTH, y*BoardSurface.TILE_LENGTH);
        sprite.draw(batch);
    }

    public void dispose () { }

    public Array<Integer> getAddress() {
        Array<Integer> pos = new Array<>();
        pos.add(x, y);
        return pos;
    }
}
