package com.apple.pawn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class UI {
    public static final String SQUARE_EXPLANATION = "square_explanation";

    private int select = -1;
    private Array<UIParts> uiParts;
    private Array<UIPartsSelect> uiPartsSelect;

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

    public void add(UIParts parts) {
        uiParts.add(parts);
//        if(((UIPartsSelect)parts).choices != null) uiPartsSelect. ※途中
    }

    public boolean remove(String name) {
        for(UIParts ui :uiParts) {
            if(ui.getName() == name) {
                ui.dispose();
                return uiParts.removeValue(ui,false);
            }
        }
        return false;
    }

    public int getSelect() {
        int s = select;
        select = -1;
        return s;
    }

    public UIParts getUIParts(String name) {
        for(UIParts ui :uiParts) {
            if(ui.getName() == name) {
                return ui;
            }
        }
        return null;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
