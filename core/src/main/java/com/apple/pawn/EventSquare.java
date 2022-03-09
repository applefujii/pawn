package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EventSquare extends Square {
    protected int move;
    protected BitmapFont font;

//    public EventSquare(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
//        super(pos, type, count, document, atlas);
//        move = MathUtils.random(1, 6);
//        if(move > BoardSurface.SQUARE_COUNT - this.count) move = BoardSurface.SQUARE_COUNT - this.count;
//
//        //確認用の仮文字
//        font = new BitmapFont(true);
//        font.setColor(0, 0, 0, 1);
//    }

    public EventSquare(Vector2 pos, int type, int count, String document) {
        super(pos, type, count, document);
        move = MathUtils.random(1, 6);
        if(move > BoardSurface.SQUARE_COUNT - this.count) move = BoardSurface.SQUARE_COUNT - this.count;

        //確認用の仮文字
        font = new BitmapFont(true);
        font.setColor(0, 0, 0, 1);
    }

    //確認用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.draw(batch, "move:" + move, pos.x*256+64, pos.y*256+64);
    }
}
