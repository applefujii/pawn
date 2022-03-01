package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fujii
 */
public class UIPartsExplanation extends UIParts {

    private String explanation;
    private Array<String> stringRow;
    private final int strWidth = 16;
    private final int strHeight = 16;
    private int charsNoRow;

    public UIPartsExplanation(String name, int x, int y, int width, int height, String expl) {
        super(name, x, y, width, height);
        explanation = expl;
        stringRow = new Array<String>();
        charsNoRow = (int)Math.floor(width/strWidth)-2;
        Matcher row = Pattern.compile("[\\s\\S]{1,"+charsNoRow+"}").matcher(expl);
        while(row.find()) {
            stringRow.add(row.group());
        }
    }

    public void initialize(Pawn game) {

    }

    public int update() {
        return -1;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.8f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        int i = 0;
        for(String s : stringRow) {
            batch.begin();
            font.draw(batch,s,x,y+strHeight*i);
            batch.end();
            i++;
        }
    }

    public void dispose () {
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
        stringRow.clear();
        Matcher row = Pattern.compile("[\\s\\S]{1,"+charsNoRow+"}").matcher(explanation);
        while(row.find()) {
            stringRow.add(row.group());
        }
    }
}
