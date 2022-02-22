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
    // ※参照にする
    private Dice dice;

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<Integer>();
        aDiceNo.setSize(10);
    }

    public void initialize(Pawn game) {
        dice = new Dice(game);
    }

    public void update() {
        piece.update();
        dice.update();
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

    public Dice getDice() {
        return dice;
    }

    public void addADiceNo(int diceNo) {
        aDiceNo.add(diceNo);
    }
}
