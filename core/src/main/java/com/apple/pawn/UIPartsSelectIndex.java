package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * 選択肢のあるUI(タイトル付き)
 * @author fujii
 */
public class UIPartsSelectIndex extends UIPartsSelect{

    private final String index;

    public UIPartsSelectIndex(String name, int x, int y, int width, int height, int group, int cursor, boolean isObstruction, String index, String... choices) {
        super(name, x, y, width, height, group, cursor, isObstruction, choices);
        this.index = index;
    }

    public void draw (@NonNull SpriteBatch batch, @NonNull ShapeRenderer renderer, @NonNull BitmapFont font) {
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
