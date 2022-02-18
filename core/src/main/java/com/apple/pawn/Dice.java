package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;

import java.time.LocalDateTime;

public class Dice {
    private BitmapFont font;
    private RandomXS128 random;
    private Texture img;        // テクスチャ
    private boolean isRoll;
    private int no;
    private Array<Integer> aNo;

    public Dice(Pawn game) {
        this.font = game.font;
        random = game.random;
        img = new Texture("badlogic.jpg");
        isRoll = false;
        no = 1;
        aNo = new Array<Integer>();
        aNo.setSize(10);
    }

    public void update() {
        if(isRoll) {
            no = random.nextInt(6) +1;
            Gdx.app.debug("info", "dice="+no);
        }
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
        font.draw(batch, Integer.toString(no), Pawn.LOGICAL_WIDTH-64, Pawn.LOGICAL_HEIGHT-64);
        batch.end();
    }

    public void dispose () {
        img.dispose();
    }

    public void rollStart() {
        if(isRoll == false) isRoll = true;
    }

    public int rollStop() {
        if(isRoll == true) {
            isRoll = false;
            aNo.add(no);
            return no;
        }
        return -1;
    }

    public boolean isRoll() {
        return isRoll;
    }

}
