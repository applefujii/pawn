package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Square {
    public static Array<String> TYPE_STR;

    protected final Sprite sprite;

    protected final Vector2 pos;
    protected final int type;
    protected final int count;
    protected String document;

    static {
        TYPE_STR = new Array<>();
        TYPE_STR.add("start", "goal", "normal", "event");
    }

    public Square(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
        this.pos = pos;
        this.type = type;
        this.count = count;
        this.document = document;
        // マスの種類
        sprite = atlas.createSprite(TYPE_STR.get(this.type));
        sprite.flip(false, true);
    }

    public void update() {}

    public void draw (Batch batch) {
        sprite.setSize(BoardSurface.TILE_LENGTH, BoardSurface.TILE_LENGTH);
        sprite.setPosition(pos.x*BoardSurface.TILE_LENGTH, pos.y*BoardSurface.TILE_LENGTH);
        sprite.draw(batch);
    }

    public void dispose () {}

    public Vector2 getAddress() {
        return pos;
    }

    public Object getDocument() {
        if(Objects.isNull(document)) return false;
        else return document;
    }

    public boolean hasEvent() {
        return (type == 1 || type == 3);
    }
}
