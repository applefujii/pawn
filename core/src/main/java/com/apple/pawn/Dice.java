package com.apple.pawn;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class Dice {
    private static final int COLS = 5;
    private static final int ROWS = 5;
    private static final int WID = 200;
    private static final int HEI = 200;

    private BitmapFont font;
    private RandomXS128 random;
    private Texture img;        // テクスチャ
    private boolean isRoll;
    private int no;
    private Array<Integer> aNo;

    public Dice(Pawn game) {
        this.font = game.font;
        random = game.random;
        isRoll = false;
        no = 1;
        aNo = new Array<>();
        aNo.setSize(10);
    }

    public void initialize(AssetManager manager) {
        img = manager.get("assets/dice.png", Texture.class);
    }

    public void update() {
        if(isRoll) {
            no = random.nextInt(6) +1;
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        if(isRoll) {
            batch.begin();
            Sprite sprite = new Sprite(new TextureRegion(img));
            sprite.setSize(200, 200);
            int x = random.nextInt(Dice.ROWS);
            int y = random.nextInt(Dice.COLS);
            sprite.setRegion(Dice.WID * x, Dice.HEI * y, Dice.WID, Dice.HEI);
            sprite.setPosition(Pawn.LOGICAL_WIDTH - 30 - 200, Pawn.LOGICAL_HEIGHT - 30 - 200);
            sprite.flip(false, true);       // 上下反転 setRegionより後に
            sprite.draw(batch);
            batch.end();
        } else {
            batch.begin();
            Sprite sprite = new Sprite(new TextureRegion(img));
            sprite.setSize(200, 200);
            int x = (no-1) %5;
            int y = (int)Math.floor(no/6);
            sprite.setRegion(Dice.WID * x, Dice.HEI * y, Dice.WID, Dice.HEI);
            sprite.setPosition(Pawn.LOGICAL_WIDTH - 30 - 200, Pawn.LOGICAL_HEIGHT - 30 - 200);
            sprite.flip(false, true);       // 上下反転 setRegionより後に
            sprite.draw(batch);
            batch.end();
        }
        /*
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

         */
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

    public int getNo() {
        return no;
    }

}
