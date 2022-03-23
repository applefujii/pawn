package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
    private int goalNo;
    @JsonProperty
    private int goalTurn;
    @JsonProperty
    private Array<Integer> aResultDetail;
    @JsonIgnore
    private final int order;

    //-- 参照
    @JsonIgnore
    private GameScreen gameScreen;
    @JsonIgnore
    private BoardSurface boardSurface;
    @JsonIgnore
    private PlayerManager playerManager;

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<Integer>();
        isGoal = false;
        goalNo = 0;
        goalTurn = 0;
        aResultDetail = new Array<>();
        aResultDetail.add(0, 0, 0);
        order = MathUtils.random(0, 255);
    }

    public void initialize(GameScreen gameScreen, BoardSurface bs, PlayerManager playerManager) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        this.playerManager = playerManager;
        piece.initialize(bs);
    }

    public void load(GameScreen gameScreen, BoardSurface bs, PlayerManager playerManager) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        this.playerManager = playerManager;
        piece.load(bs);
    }

    public void update() {
        if(piece.update()) {
            gameScreen.addGoalNo();
            goalNo = gameScreen.getGoalNo();
            goalTurn = gameScreen.getTurnCount();
            isGoal = true;
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        piece.draw(batch, renderer);
    }

    public void dispose () {
        piece.dispose();
    }

    public void addResultDetail(Square visitSquare) {
        int type = visitSquare.getType() - 2;
        if(type >= 0) {
            int count = aResultDetail.get(type);
            count++;
            aResultDetail.insert(type, count);
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGoalNo() {
        return goalNo;
    }

    public int getGoalTurn() {
        return goalTurn;
    }

    public int getOrder() {
        return order;
    }
}
