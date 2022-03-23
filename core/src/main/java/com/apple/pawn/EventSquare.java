package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EventSquare extends Square {
    protected BitmapFont font;

    public EventSquare(Vector2 pos, int type, int count) {
        this(pos, type, count, MathUtils.random(1, 6));
    }

    public EventSquare(Vector2 pos, int type, int count, int move) {
        super(pos, type, count);
        this.move = Math.min(move, BoardSurface.SQUARE_COUNT - this.count);

        document = move+"マス進む";

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
