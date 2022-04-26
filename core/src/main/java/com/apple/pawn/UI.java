package com.apple.pawn;

import android.support.annotation.NonNull;

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
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()) {
            UIParts ui = uiPartsIterator.next();
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

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font, int group) {
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()){
            UIParts ui = uiPartsIterator.next();
            if(ui.group != group) continue;
            ui.draw(batch,renderer,font);
        }
        Iterator<UIPartsSelect> uiPartsSelectIterator = new Array.ArrayIterator<>(uiPartsSelect);
        while(uiPartsSelectIterator.hasNext()){
            UIPartsSelect ui = uiPartsSelectIterator.next();
            if(ui.group != group) continue;
            ui.draw(batch,renderer,font);
        }
        if(dice != null  &&  group == 1) dice.draw(batch);
    }

    public void dispose () {
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()){
            UIParts ui = uiPartsIterator.next();
            ui.dispose();
        }
        Iterator<UIPartsSelect> uiPartsSelectIterator = new Array.ArrayIterator<>(uiPartsSelect);
        while(uiPartsSelectIterator.hasNext()){
            UIPartsSelect ui = uiPartsSelectIterator.next();
            ui.dispose();
        }
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

    public UIParts getUIParts(String name) {
        Iterator<UIParts> uiPartsIterator = new Array.ArrayIterator<>(uiParts);
        while(uiPartsIterator.hasNext()) {
            UIParts ui = uiPartsIterator.next();
            if(ui.getName().equals(name)) {
                return ui;
            }
        }
        Iterator<UIPartsSelect> uiPartsSelectIterator = new Array.ArrayIterator<>(uiPartsSelect);
        while(uiPartsSelectIterator.hasNext()) {
            UIPartsSelect ui = uiPartsSelectIterator.next();
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
