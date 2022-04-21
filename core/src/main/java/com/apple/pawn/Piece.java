package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fujii
 */
public class Piece {
    public static final String[] COLOR = {"red", "yellow", "green", "light_blue", "blue", "purple"};
    public static final int WIDTH = 80, HEIGHT = 120;
    private static final int LINE_MAX = 3;
    @JsonIgnore
    private float MOVE_INTERVAL = 0.35f;

    @JsonProperty
    private int colorNo;       // 色番号
    @JsonProperty
    private int squareNo;       // 現在何マス目か
    @JsonProperty
    private int moveToSquareNo;
    @JsonProperty
    private Vector2 pos;
    @JsonProperty
    private Vector2 camPos;
    @JsonProperty
    private Vector2 pastPos;
    @JsonProperty
    private Vector2 moveToPos;
    @JsonProperty
    private float timer;
    @JsonProperty
    private boolean isTimer;
    @JsonProperty
    private boolean isMove;

    //-- リソース
    @JsonIgnore
    private Sprite sprite;

    //-- 参照
    @JsonIgnore
    private BoardSurface boardSurface;

    public Piece() {
    }

    public Piece(int pieceColorNo) {
        this.colorNo = pieceColorNo;
        squareNo = 0;
        moveToSquareNo = 0;
        pos = new Vector2();
        camPos = new Vector2();
        moveToPos = new Vector2();
        timer = 0;
        isTimer = false;
        isMove = false;
    }

    public void initialize(BoardSurface bs, AssetManager manager) {
        this.boardSurface = bs;
        boardSurface.getSquare(squareNo).addPiece(this);
        pos = bs.getPos(squareNo);
        pos.x += WIDTH*((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))%LINE_MAX);
        pos.y += HEIGHT*Math.floor((float) boardSurface.getSquare(squareNo).aPiece.indexOf(this,true)/LINE_MAX);
        camPos = pos.cpy();
        sprite = manager.get("assets/piece_atlas.txt", TextureAtlas.class).createSprite(COLOR[this.colorNo]);
        sprite.flip(false, true);
    }

    public void load(BoardSurface bs, AssetManager manager) {
        this.boardSurface = bs;
        boardSurface.getSquare(squareNo).addPiece(this);
        sprite = manager.get("assets/piece_atlas.txt", TextureAtlas.class).createSprite(COLOR[this.colorNo]);
        sprite.flip(false, true);
    }

    public boolean update() {
        if(isMove) {
            //-- 進める
            if (!isTimer) {
                int plus = 1;
                if(squareNo > moveToSquareNo) plus = -1;
                if(squareNo != moveToSquareNo) {
                    boardSurface.getSquare(squareNo).removePiece(this);
                    squareNo+=plus;
                    boardSurface.getSquare(squareNo).addPiece(this);
                }
                pastPos = pos.cpy();
                moveToPos = boardSurface.getPos(squareNo);
                // 駒同士が重ならないようにずらす
                moveToPos.x += WIDTH*((boardSurface.getSquare(squareNo).aPiece.indexOf(this,true))%LINE_MAX);
                moveToPos.y += HEIGHT*Math.floor((float) boardSurface.getSquare(squareNo).aPiece.indexOf(this,true)/LINE_MAX);
                isTimer = true;
            }
            //-- タイマー
            else {
                timer += Gdx.graphics.getDeltaTime();
                //-- 時間が経っていない
                if(timer < MOVE_INTERVAL) {
                    //-- 動くアニメーション
                    float progress = (timer/MOVE_INTERVAL)*1.2f;
                    if(progress>1.0f) progress =1.0f;
                    // 3次関数補間で動かす
                    pos.x = pastPos.x + (moveToPos.x-pastPos.x)*(float)Math.pow(progress, 2)*(3-2*progress);
                    pos.y = pastPos.y + (moveToPos.y-pastPos.y)*(float)Math.pow(progress, 2)*(3-2*progress);
                    camPos = pos.cpy();
                    // 上下動作
                    pos.y += Math.sin(Math.toRadians(180+(180*progress)))*60;
                }
                //-- 時間が経ったら
                else {
                    timer = 0;
                    isTimer = false;
                    pos = moveToPos;
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
        } else if(!FlagManagement.is(Flag.PIECE_MOVE)) {
            pos = boardSurface.getPos(squareNo);
            pos.x += WIDTH * ((boardSurface.getSquare(squareNo).aPiece.indexOf(this, true)) % LINE_MAX);
            pos.y += HEIGHT * Math.floor((float) boardSurface.getSquare(squareNo).aPiece.indexOf(this, true) / LINE_MAX);
            camPos = pos.cpy();
        }
        return false;
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        sprite.setSize(WIDTH, HEIGHT);
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);

//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.BLACK);
//        renderer.circle(pos.x, pos.y, 32);
//        renderer.end();
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.RED);
//        renderer.circle(pos.x, pos.y, 29);
//        renderer.end();
    }

    public void dispose () { }

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
        return pos;
    }

    public Vector2 getCameraPosition() {
        return camPos;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
