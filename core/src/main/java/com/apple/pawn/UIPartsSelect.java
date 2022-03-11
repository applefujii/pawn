package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class UIPartsSelect extends UIParts {
    protected Array<String> choices;
    protected int cursor = 0;
    private boolean isObstruction;

    public UIPartsSelect(String name, int x, int y, int width, int height, boolean isObstruction, String... choices) {
        super(name, x, y, width, height);
        this.isObstruction = isObstruction;
        this.choices = new Array<String>();
        for(String cho : choices) {
            this.choices.add(cho);
        }
        if(isObstruction) FlagManagement.fold(Flag.INPUT_ENABLE);
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
        renderer.setColor(1.0f,0.4f,0.4f,0.5f);
        renderer.box(x,y+height*cursor,0,width,height,0);
        renderer.end();

        int i = 0;
        Array.ArrayIterator<String> choicesIterator = new Array.ArrayIterator<>(choices);
        batch.begin();
        while(choicesIterator.hasNext()) {
            String cho = choicesIterator.next();
            font.draw(batch,cho,x,y+height*i);
            i++;
        }
        batch.end();
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

    public int getCursor() {
        return cursor;
    }
}
