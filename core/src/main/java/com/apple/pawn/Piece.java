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
    private static final String[] COLOR = {"red", "yellow", "green", "light_blue", "blue", "purple"};
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
        pos = bs.getPos(squareNo);
    }

    public boolean update() {
        if(isMove) {
            if (isTimer == false) {
                int plus = 1;
                if(squareNo > moveToSquareNo) plus = -1;
                if(squareNo != moveToSquareNo) squareNo+=plus;
                pos = boardSurface.getPos(squareNo);
                isTimer = true;
            } else {
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
        return false;
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        sprite.setSize(80, 120);
        sprite.setPosition(pos.x, pos.y);
        Gdx.app.debug("info", sprite.toString());
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

    public void move( int squareNo ) {
        this.moveToSquareNo += squareNo;
        if(moveToSquareNo < 0) moveToSquareNo = 0;
        if(moveToSquareNo > boardSurface.getSquareCount()-1) moveToSquareNo = boardSurface.getSquareCount()-1;
        isMove = true;
        FlagManagement.set(Flag.PIECE_MOVE);
    }

    public void setSquareNo(int squareNo) {
        this.squareNo = squareNo;
        this.moveToSquareNo = squareNo;
    }

    public Vector2 getPosition() {
        return boardSurface.getPos(squareNo);
    }
}
