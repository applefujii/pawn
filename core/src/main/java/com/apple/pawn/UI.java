package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class UI {
    public static int select = -1;
    private Array<UIParts> uiParts;

    //-- 参照
    private Pawn game;
    private Dice dice;

    private Texture img;        // テクスチャ

    public UI() {
        uiParts = new Array<UIParts>();
        img = new Texture("badlogic.jpg");
    }

    public void initialize(Pawn game) {
        this.game = game;
    }

    public int update() {
        for(UIParts ui : uiParts) {
            int ret = ui.update();
            if( ret != -1 ) {
                select = ret;
                uiParts.removeValue(ui, false);
                return ret;
            }
        }
        return -1;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        for(UIParts pa : uiParts) {
            pa.draw(batch,renderer,font);
        }
        if(dice != null) dice.draw(batch, renderer);
    }

    public void dispose () {
        img.dispose();
    }

    public void addUiParts(UIParts parts) {
        uiParts.add(parts);
    }

    public boolean removeUiParts(String name) {
        for(UIParts ui :uiParts) {
            if(ui.getName() == name) {
                return uiParts.removeValue(ui,false);
            }
        }
        return false;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
