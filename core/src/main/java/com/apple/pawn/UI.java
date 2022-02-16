package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UI {
    private Dice dice;

    private BitmapFont font;
    private Texture img;        // テクスチャ

    public UI(BitmapFont font) {
        this.font = font;
        img = new Texture("badlogic.jpg");
    }

    public void initialize(Dice dice) {
        this.dice = dice;
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        dice.draw(batch, renderer);
    }

    public void dispose () {
        img.dispose();
    }
}
