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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fujii
 */
public class Player {
    @JsonProperty
    private String name;                // 名前
    @JsonProperty
    private Piece piece;                // 駒
    @JsonProperty
    private Array<Integer> aDiceNo;     // 過去に出したサイコロの目
    @JsonProperty
    private boolean isGoal;             // ゴールしているか
    @JsonProperty
    private int goalNo;                 // 何番目にゴールしたか

    //-- 参照
    @JsonIgnore
    private GameScreen gameScreen;
    @JsonIgnore
    private BoardSurface boardSurface;

    public Player() {
    }

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<Integer>();
        isGoal = false;
        goalNo = 0;
    }

    public void initialize(GameScreen gameScreen, BoardSurface bs) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        piece.initialize(bs);
    }

    public void load(GameScreen gameScreen, BoardSurface bs) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        piece.load(bs);
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
        if(aDiceNo.size >=2) aDiceNo.removeIndex(0);
        aDiceNo.add(diceNo);
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
