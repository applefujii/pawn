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
    protected boolean[] enable;
    protected int cursor = 0;
    private boolean isObstruction;

    public UIPartsSelect(String name, int x, int y, int width, int height, int group, int cursor, boolean isObstruction, String... choices) {
        super(name, x, y, width, height, group);
        this.cursor = cursor;
        this.isObstruction = isObstruction;
        enable = new boolean[choices.length];
        this.choices = new Array<String>();
        int i = 0;
        for(String cho : choices) {
            if(cho.startsWith("/")) {
                enable[i] = false;
                this.choices.add(cho.substring(1));
            } else {
                enable[i] = true;
                this.choices.add(cho);
            }
            i++;
        }
        if(enable[this.cursor] == false) {
            this.cursor = 0;
            while(true) {
                if(enable[this.cursor] == true) break;
                this.cursor++;
            }
        }
        if(isObstruction) FlagManagement.fold(Flag.INPUT_ENABLE);
    }

    public int update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(Input.Keys.W)) selectUp();
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyJustPressed(Input.Keys.S)) selectDown();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER) ) return confirm();
        return -2;
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
            if(enable[i]) {
                font.draw(batch, cho, x, y + height * i);
            } else {
                Color col = font.getColor().cpy();
                font.setColor(0.3f,0.3f,0.3f,1);
                font.draw(batch, cho, x, y + height * i);
                font.setColor(col);
            }
            i++;
        }
        batch.end();
    }

    public int selectUp() {
        do {
            cursor--;
            if(cursor < 0) {
                cursor = 0;
                while(true) {
                    if(enable[cursor] == true) return cursor;
                    cursor++;
                }
            }
        } while(enable[cursor] == false);
        return cursor;
    }

    public int selectDown() {
        do {
            cursor++;
            if(cursor > choices.size-1) {
                cursor = choices.size-1;
                while(true) {
                    if(enable[cursor] == true) return cursor;
                    cursor--;
                }
            }
        } while(enable[cursor] == false);
        return cursor;
    }

    public int confirm() {
        if(isObstruction) FlagManagement.set(Flag.INPUT_ENABLE);
        return cursor;
    }

    public void dispose () {
        if(isObstruction) FlagManagement.set(Flag.INPUT_ENABLE);
    }

    public int getCursor() {
        return cursor;
    }
}
