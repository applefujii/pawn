package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * @author fujii
 */
public class Piece {
    public static final String[] COLOR = {"red", "yellow", "green", "light_blue", "blue", "purple"};
    public static final int WIDTH = 80, HEIGHT = 120;
    private static final int LINE_MAX = 3;
    private float MOVE_INTERVAL = 0.3f;

    private int squareNo;       // 現在何マス目か
    private int moveToSquareNo;
    private Vector2 pos;
    private Vector2 moveToPos;
    private float timer;
    private boolean isTimer;
    private boolean isMove;

    //-- リソース
    private final Sprite sprite;

    //-- 参照
    private BoardSurface boardSurface;

    public Piece(int pieceColorNo) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("piece_atlas.txt"));
        sprite = atlas.createSprite(COLOR[pieceColorNo]);
        sprite.flip(false, true);
        squareNo = 0;
        moveToSquareNo = 0;
        pos = new Vector2();
        moveToPos = new Vector2();
        timer = 0;
        isTimer = false;
        isMove = false;
    }

    public void initialize(BoardSurface bs) {
        this.boardSurface = bs;
        boardSurface.getSquare(squareNo).addPiece(this);
        pos = bs.getPos(squareNo);
        pos.x += WIDTH*((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))%LINE_MAX);
        pos.y += HEIGHT*Math.floor((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))/LINE_MAX);
    }

    public boolean update() {
        if(isMove) {
            //-- 進める
            if (isTimer == false) {
                int plus = 1;
                if(squareNo > moveToSquareNo) plus = -1;
                if(squareNo != moveToSquareNo) {
                    boardSurface.getSquare(squareNo).removePiece(this);
                    squareNo+=plus;
                    boardSurface.getSquare(squareNo).addPiece(this);
                }
                pos = boardSurface.getPos(squareNo);
                pos.x += WIDTH*((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))%LINE_MAX);
                pos.y += HEIGHT*Math.floor((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))/LINE_MAX);
                isTimer = true;
            }
            //-- タイマー
            else {
                timer += Gdx.graphics.getDeltaTime();
                if (timer >= MOVE_INTERVAL) {
                    timer = 0;
                    isTimer = false;
                    if (squareNo >= boardSurface.getSquareCount()-1) {
                        isMove = false;
                        FlagManagement.fold(Flag.PIECE_MOVE);
                        return true;
                    }
                    if (squareNo == moveToSquareNo) {
                        isMove = false;
                        FlagManagement.fold(Flag.PIECE_MOVE);
                    }
                }
            }
        }
        pos = boardSurface.getPos(squareNo);
        pos.x += WIDTH*((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))%LINE_MAX);
        pos.y += HEIGHT*Math.floor((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))/LINE_MAX);
        return false;
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        sprite.setSize(WIDTH, HEIGHT);
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);
        batch.end();

//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.BLACK);
//        renderer.circle(pos.x, pos.y, 32);
//        renderer.end();
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.RED);
//        renderer.circle(pos.x, pos.y, 29);
//        renderer.end();
    }

    public void dispose () {
    }

    public void move( int squareNo, boolean isAnimation ) {
        //-- アニメーションさせる
        if(isAnimation) {
            this.moveToSquareNo += squareNo;
            if (moveToSquareNo < 0) moveToSquareNo = 0;
            if (moveToSquareNo > boardSurface.getSquareCount() - 1)
                moveToSquareNo = boardSurface.getSquareCount() - 1;
            isMove = true;
            FlagManagement.set(Flag.PIECE_MOVE);
        }
        //-- アニメーションさせない
        else {
            boardSurface.getSquare(squareNo).removePiece(this);
            this.squareNo = squareNo;
            this.moveToSquareNo = squareNo;
            boardSurface.getSquare(squareNo).addPiece(this);
        }
    }

    public int getSquareNo() {
        return squareNo;
    }

    public Vector2 getPosition() {
        return boardSurface.getPos(squareNo);
    }
}
