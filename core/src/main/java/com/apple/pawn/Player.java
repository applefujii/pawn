package com.apple.pawn;

import com.badlogic.gdx.Gdx;
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
    private boolean isGoal;
    private int goalNo;

    //-- 参照
    private GameScreen gameScreen;
    private BoardSurface boardSurface;

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<Integer>();
        aDiceNo.setSize(10);
        isGoal = false;
        goalNo = 0;
    }

    public void initialize(GameScreen gameScreen, BoardSurface bs) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        piece.initialize(bs);
    }

    public void update() {
        if(piece.update()) {
            gameScreen.addGoalNo();
            goalNo = gameScreen.getGoalNo();
            isGoal = true;
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        piece.draw(batch, renderer);
    }

    public void dispose () {
    }

    public void addADiceNo(int diceNo) {
        aDiceNo.add(diceNo);
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Piece getPiece() {
        return piece;
    }

    public String getName() {
        return name;
    }

}
