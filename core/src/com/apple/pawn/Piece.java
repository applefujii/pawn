package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Piece extends Actor {
    private Texture img;        // テクスチャ
    private int squareNo;       // 現在何マス目か

    public Piece() {
        img = new Texture("badlogic.jpg");
    }

    public void move( int squareNo ) {
        this.squareNo = squareNo;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * 描画
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(32, 32);
//        sprite.setRegion(100,100,100,100);
        sprite.setPosition(0, 0);   // ※仮
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
    }

    public void dispose () {
        img.dispose();
    }

}
