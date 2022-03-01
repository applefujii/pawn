package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

abstract public class UIParts {
    protected String name;
    protected int x, y;
    protected int width, height;

    public UIParts(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    abstract public void initialize(Pawn game);

    abstract public int update();

    abstract public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font);

    abstract public void dispose ();

    public String getName() {
        return name;
    }
}
