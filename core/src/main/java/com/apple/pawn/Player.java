package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class Player {
    private final String name;
    private final Piece piece;
    private final Array<Integer> aDiceNo;
    private boolean isGoal;
    private int goalNo;
    private int goalTurn;
    private final Array<ResultDetail> resultDetails;

    //-- 参照
    private GameScreen gameScreen;
    private BoardSurface boardSurface;
    private PlayerManager playerManager;

    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<>();
        aDiceNo.setSize(10);
        isGoal = false;
        goalNo = 0;
        goalTurn = 0;
        resultDetails = new Array<>();
    }

    public void initialize(GameScreen gameScreen, BoardSurface bs, PlayerManager playerManager) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        this.playerManager = playerManager;
        piece.initialize(bs);
    }

    public void update() {
        if(piece.update()) {
            gameScreen.addGoalNo();
            goalNo = gameScreen.getGoalNo();
            goalTurn = resultDetails.size;
            isGoal = true;
            playerManager.addGoal(this);
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        piece.draw(batch, renderer);
    }

    public void dispose () {
        piece.dispose();
    }

    public void addResultDetail(Square square, int turn) {
        resultDetails.add(new ResultDetail(square, turn));
    }

    public ResultDetail getResultDetail(int turn) {
        if(turn < 1) return resultDetails.first();
        else if(turn > resultDetails.size) return resultDetails.peek();
        else return resultDetails.get(turn-1);
    }

    public Array<ResultDetail> getResultDetails() {
        return resultDetails;
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

    public int getGoalNo() {
        return goalNo;
    }

    public int getGoalTurn() {
        return goalTurn;
    }
}
