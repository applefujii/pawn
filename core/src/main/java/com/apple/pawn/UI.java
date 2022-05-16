package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import java.util.Iterator;

/**
 * UI管理クラス
 * @author fujii
 */
public class UI {
    public static final String SQUARE_EXPLANATION = "square_explanation";
    public static final String OPERATING_METHOD = "operating_method";

    private int select = -1;
    private final Array<UIParts> uiParts;
    private final Array<UIPartsSelect> uiPartsSelect;

    //-- 参照
    private Pawn game;
    private Dice dice;

    public UI() {
        uiParts = new Array<>();
        uiPartsSelect = new Array<>();
    }

    public void initialize(Pawn game) {
        this.game = game;
    }

    public int update() {
        for(UIParts ui : uiParts) {
            if(ui.update() == -1) {
                remove(ui.getName());
            }
        }
        if(uiPartsSelect.size > 0) {
            UIPartsSelect ui = uiPartsSelect.peek();
            int ret = ui.update();
            if (ret == -1) {
                remove(ui.getName());
            } else {
                if (ret != -2) {
                    select = ret;
                    remove(ui.getName());
                }
            }
        }
        return 0;
    }

    public void draw (SpriteBatch batch, ShapeRenderer renderer, BitmapFont font, int group) {
        for(UIParts ui : uiParts) {
            if(ui.group != group) continue;
            ui.draw(batch,renderer,font);
        }
        for(UIPartsSelect ui : uiPartsSelect) {
            if(ui.group != group) continue;
            ui.draw(batch,renderer,font);
        }
        if(dice != null  &&  group == 1) dice.draw(batch);
    }

    public void dispose () {
        for(UIParts ui : uiParts) ui.dispose();
        for(UIPartsSelect ui : uiPartsSelect) ui.dispose();
    }

    public void add(@NonNull UIParts parts) {
        if(parts.getClass().getName().matches(".*Select.*") == true) uiPartsSelect.add((UIPartsSelect)parts);
        else uiParts.add(parts);
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
        Iterator<UIPartsSelect> uiPartsSelectIterator = new Array.ArrayIterator<>(uiPartsSelect);
        while(uiPartsSelectIterator.hasNext()) {
            UIPartsSelect ui = uiPartsSelectIterator.next();
            if(ui.getName().equals(name)) {
                ui.dispose();
                uiPartsSelectIterator.remove();
                return true;
            }
        }
        return false;
    }

    public void removeAllSelect() {
        uiPartsSelect.clear();
    }

    public int getSelect() {
        int s = select;
        select = -1;
        return s;
    }

    public int getCursor() {
        int ret = -1;
        if(uiPartsSelect.size > 0) ret = uiPartsSelect.peek().getCursor();
        return ret;
    }

    public @Null UIParts getUIParts(String name) {
        for(UIParts ui : uiParts) {
            if(ui.getName().equals(name)) {
                return ui;
            }
        }
        for(UIPartsSelect ui : uiPartsSelect) {
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
