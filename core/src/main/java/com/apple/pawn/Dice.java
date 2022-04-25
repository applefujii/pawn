package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class Dice {
    private static final int COLS = 5, ROWS = 5;
    private static final int WID = 200, HEI = 200;

    private final RandomXS128 random;
    private Texture img;        // テクスチャ
    private boolean isRoll;
    private int no;
    private final Array<Integer> aNo;

    public Dice(@NonNull Pawn game) {
        random = game.random;
        isRoll = false;
        no = 1;
        aNo = new Array<>();
        aNo.setSize(10);
    }

    public void initialize(@NonNull AssetManager manager) {
        img = manager.get("assets/dice.png", Texture.class);
    }

    public void update() {
        if(isRoll) {
            no = random.nextInt(6) +1;
        }
    }

    public void draw (Batch batch) {
        if(isRoll) {
            batch.begin();
            Sprite sprite = new Sprite(new TextureRegion(img));
            sprite.setSize(200, 200);
            int x = random.nextInt(ROWS);
            int y = random.nextInt(COLS);
            sprite.setRegion(WID * x, HEI * y, WID, HEI);
            sprite.setPosition(Pawn.LOGICAL_WIDTH - 30 - 200, Pawn.LOGICAL_HEIGHT - 30 - 200);
            sprite.flip(false, true);       // 上下反転 setRegionより後に
            sprite.draw(batch);
            batch.end();
        } else {
            batch.begin();
            Sprite sprite = new Sprite(new TextureRegion(img));
            sprite.setSize(200, 200);
            int x = (no-1) %5;
            int y = (int) no / 6;
            sprite.setRegion(WID * x, HEI * y, WID, HEI);
            sprite.setPosition(Pawn.LOGICAL_WIDTH - 30 - 200, Pawn.LOGICAL_HEIGHT - 30 - 200);
            sprite.flip(false, true);       // 上下反転 setRegionより後に
            sprite.draw(batch);
            batch.end();
        }
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

    public int getNo() {
        return no;
    }

}
