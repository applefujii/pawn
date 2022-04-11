package com.apple.pawn;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class EventSquare extends Square {
    protected BitmapFont font;

    public EventSquare(Vector2 coo, int type, int count, int move) {
        super(coo, type, count);
        this.move = move;

        //確認用の仮文字
        font = new BitmapFont(true);
        font.setColor(0, 0, 0, 1);
    }

    @Override
    public void initialize(AssetManager manager, int size) {
        super.initialize(manager, size);
        move = Math.min(move, size - count);
        document = move+"マス進む";
    }
}
