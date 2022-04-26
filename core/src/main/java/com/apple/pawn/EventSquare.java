package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EventSquare extends Square {
    public EventSquare(Vector2 coo, int type, int count, int move) {
        super(coo, type, count);
        this.move = move;
    }

    @Override
    public void initialize(@NonNull AssetManager manager, int size, int no, BitmapFont font) {
        super.initialize(manager, size, no, font);
        move = Math.min(move, size - count);
        document = move+"マス進む";
        uiDoc = uiDoc + "\n\n" + document;
    }

    @Override
    public void drawFont(SpriteBatch batch) {
        if(!FlagManagement.is(Flag.LOOK_MAP)) {
            font.draw(batch, document, position.x + 16, position.y + 16);
        }
    }
}
