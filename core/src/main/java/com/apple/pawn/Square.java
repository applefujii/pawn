package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Square {
    public static Array<String> TYPE_STR;

    protected final Vector2 pos;
    protected final int type;
    protected final int count;
    protected String document;
    protected int move;
    protected int back;

    //-- 参照
    protected Array<Piece> aPiece;

    static {
        TYPE_STR = new Array<>();
        TYPE_STR.add("start", "goal", "normal", "event");
        TYPE_STR.add("task");
    }

//    public Square(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
    public Square(Vector2 pos, int type, int count) {
        this.pos = pos;
        this.type = type;
        this.count = count;
        aPiece = new Array<>();
    }

    public void update() {}

    public void draw (Batch batch) { }

    public void dispose () { }

    public void addPiece(Piece piece) {
        aPiece.add(piece);
    }

    public void removePiece(Piece piece) {
        aPiece.removeValue(piece, false);
    }

    public Vector2 getAddress() {
        return new Vector2(BoardSurface.TILE_WIDTH*pos.x, BoardSurface.TILE_HEIGHT*pos.y);
    }

    public boolean hasDocument() {
        return Objects.nonNull(document);
    }

    public String getDocument() {
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
