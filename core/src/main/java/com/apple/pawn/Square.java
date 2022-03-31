package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import java.util.Objects;

public class Square {
    public static final String[] TYPE_STR = {"start", "goal", "normal", "event", "task"};
    public static final int SQUARE_WIDTH = 256, SQUARE_HEIGHT = 256;

    protected final Vector2 coordinate;
    protected final Vector2 position;
    protected final int type;
    protected final int count;

    protected Sprite sprite;
    protected String document;
    protected int move;
    protected int back;

    //-- 参照
    protected Array<Piece> aPiece;

    public Square(Vector2 coo, int type, int count) {
        coordinate = coo;
        position = new Vector2(SQUARE_WIDTH*coordinate.x, SQUARE_HEIGHT*coordinate.y);
        this.type = type;
        this.count = count;
        aPiece = new Array<>();
    }

    public void initialize(TextureAtlas atlas, int size) {
        sprite = atlas.createSprite(TYPE_STR[type]);
        sprite.flip(false, true);
        sprite.setPosition(position.x, position.y);
    }

    public void update() { }

    public void draw (Batch batch) {
        sprite.draw(batch);
    }

    public void dispose () { }

    public void addPiece(Piece piece) {
        aPiece.add(piece);
    }

    public void removePiece(Piece piece) {
        aPiece.removeValue(piece, false);
    }

    public Vector2 getPos() {
        return position.cpy();
    }

    public boolean hasDocument() {
        return Objects.nonNull(document);
    }

    public @Null String getDocument() {
        return document;
    }

    public int getType() {
        return type;
    }

    public int getMove() {
        return move;
    }

    public int getBack() {
        return back;
    }
}
