package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class Player {
    private String name;
    private Piece piece;
    private Array<Integer> aDiceNo;

    //-- 参照
    private BoardSurface boardSurface;

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<Integer>();
        aDiceNo.setSize(10);
    }

    public void initialize(BoardSurface bs) {
        this.boardSurface = bs;
        piece.initialize(bs);
    }

    public void update() {
        piece.update();
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        piece.draw(batch, renderer);
    }

    public void dispose () {
    }

    public Piece getPiece() {
        return piece;
    }

    public String getName() {
        return name;
    }

    public void addADiceNo(int diceNo) {
        aDiceNo.add(diceNo);
    }
}
