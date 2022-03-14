package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UIPartsSelectIndex extends UIPartsSelect{

    private String index;

    public UIPartsSelectIndex(String name, int x, int y, int width, int height, int cursor, boolean isObstruction, String index, String... choices) {
        super(name, x, y, width, height, cursor, isObstruction, choices);
        this.index = index;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.8f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height*(choices.size+1),0);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.5f,0.5f,0.5f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1.0f,0.4f,0.4f,0.5f);
        renderer.box(x,y+height*(cursor+1),0,width,height,0);
        renderer.end();

        batch.begin();
        font.draw(batch,index,x,y);
        batch.end();

        int i = 0;
        for(String cho : choices) {
            batch.begin();
            font.draw(batch,cho,x,y+height*(i+1));
            batch.end();
            i++;
        }
    }

}
