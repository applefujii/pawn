package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Piece {
    private Texture img;        // テクスチャ
    private int squareNo;       // 現在何マス目か

    public Piece() {
        img = new Texture("badlogic.jpg");
        squareNo = 0;
    }

    public void initialize(Pawn game) {

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
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.circle(50+squareNo*100, 100, 32);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.circle(50+squareNo*100, 100, 29);
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
}
