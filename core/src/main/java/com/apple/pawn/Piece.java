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
    private Texture img;        // テクスチャ
    private int squareNo;       // 現在何マス目か
    private final TextureAtlas atlas;

    //-- 参照
    private BoardSurface boardSurface;

    public Piece(int pieceColorNo) {
        atlas = new TextureAtlas(Gdx.files.internal("map_atlas.txt"));
        img = new Texture("badlogic.jpg");
        squareNo = 0;
    }

    public void initialize(BoardSurface bs) {
        this.boardSurface = bs;
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
//        batch.begin();
//        Sprite sprite = new Sprite( new TextureRegion(img));
//        sprite.setSize(32, 32);
////        sprite.setRegion(100,100,100,100);
//        sprite.setPosition(squareNo*50, 0);   // ※仮
//        sprite.flip(false, true);       // 上下反転 setRegionより後に
//        sprite.draw(batch);
//        batch.end();
        Vector2 pos = boardSurface.getPos(squareNo);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.circle(pos.x, pos.y, 32);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.circle(pos.x, pos.y, 29);
        renderer.end();
    }

    public void dispose () {
        img.dispose();
    }

    public void move( int squareNo ) {
        this.squareNo += squareNo;
    }

    public void setSquareNo(int squareNo) {
        this.squareNo = squareNo;
    }

    public Vector2 getPosition() {
        return boardSurface.getPos(squareNo);
    }
}
