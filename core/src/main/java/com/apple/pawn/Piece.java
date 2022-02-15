package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Piece {
    private Texture img;        // テクスチャ
    private int squareNo;       // 現在何マス目か
    private String name;        // 名前

    public Piece( String name ) {
        this.name = name;
        img = new Texture("badlogic.jpg");
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(32, 32);
//        sprite.setRegion(100,100,100,100);
        sprite.setPosition(squareNo*50, 0);   // ※仮
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
        batch.end();
    }

    public void dispose () {
        img.dispose();
    }

    public void move( int squareNo ) {
        this.squareNo = squareNo;
    }

    public void setSquareNo(int squareNo) {
        this.squareNo = squareNo;
    }
}
