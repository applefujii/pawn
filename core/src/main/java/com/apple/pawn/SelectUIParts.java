package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class SelectUIParts extends UIParts {
    private Array<String> choices;
    private int cursor = 0;
    private int width, height;

    public SelectUIParts(String name, int x, int y, String... choices) {
        super(name, x, y);
        this.choices = new Array<String>();
        for(String cho : choices) {
            this.choices.add(cho);
        }
        width = 300;
        height = 16;
        FlagManagement.fold(Flag.INPUT_ENABLE);
    }

    public void initialize(Pawn game) {

    }

    public int update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectUp();
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectDown();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) return confirm();
        return -1;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.8f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height*choices.size,0);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,1,1,0.5f);
        renderer.box(x,y+height*cursor,0,width,height,0);
        renderer.end();

        int i = 0;
        for(String cho : choices) {
            batch.begin();
            font.draw(batch,cho,x,y+height*i);
            batch.end();
            i++;
        }
    }

    public int selectUp() {
        cursor--;
        if(cursor < 0) cursor = 0;
        return cursor;
    }

    public int selectDown() {
        cursor++;
        if(cursor > choices.size-1) cursor = choices.size-1;
        return cursor;
    }

    public int confirm() {
        FlagManagement.set(Flag.INPUT_ENABLE);
        return cursor;
    }

    public void dispose () {
    }

}
