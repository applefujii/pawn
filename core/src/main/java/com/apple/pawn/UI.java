package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * @author fujii
 */
public class UI {
    public static final String SQUARE_EXPLANATION = "square_explanation";

    private int select = -1;
    private final Array<UIParts> uiParts;
    private Array<UIPartsSelect> uiPartsSelect;

    //-- 参照
    private Pawn game;
    private Dice dice;

    private final Texture img;        // テクスチャ

    public UI() {
        uiParts = new Array<>();
        img = new Texture("badlogic.jpg");
    }

    public void initialize(Pawn game) {
        this.game = game;
    }

    public int update() {
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()) {
            UIParts ui = uiPartsIterator.next();
            int ret = ui.update();
            if( ret != -1 ) {
                select = ret;
                uiPartsIterator.remove();
                return ret;
            }
        }
        return -1;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()){
            UIParts ui = uiPartsIterator.next();
            ui.draw(batch,renderer,font);
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
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()) {
            UIParts ui = uiPartsIterator.next();
            if(ui.getName().equals(name)) {
                ui.dispose();
                uiPartsIterator.remove();
                return true;
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
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()) {
            UIParts ui = uiPartsIterator.next();
            if(ui.getName().equals(name)) {
                return ui;
            }
        }
        return null;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
