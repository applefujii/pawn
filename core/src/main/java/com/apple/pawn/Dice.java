package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Dice {
    private BitmapFont font;
    private Texture img;        // テクスチャ
    private Array<Integer> aNo;

    public Dice(BitmapFont font) {
        this.font = font;
        img = new Texture("badlogic.jpg");
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
        renderer.box(Pawn.LOGICAL_WIDTH-100, Pawn.LOGICAL_HEIGHT-100, 0, 84, 84, 0);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.box(Pawn.LOGICAL_WIDTH-98, Pawn.LOGICAL_HEIGHT-98, 0, 80, 80, 0);
        renderer.end();

        batch.begin();
        font.draw(batch, "1", Pawn.LOGICAL_WIDTH-64, Pawn.LOGICAL_HEIGHT-64);
        batch.end();
    }

    public void dispose () {
        img.dispose();
    }

}
