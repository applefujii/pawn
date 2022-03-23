package com.apple.pawn;

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
    private final Array<String> stringRow;
    private final int strWidth = 18;
    private final int strHeight = 18;
    private final int charsNoRow;

    public UIPartsExplanation(String name, int x, int y, int width, int height, String expl) {
        super(name, x, y, width, height);
        explanation = expl;
        stringRow = new Array<>();
        charsNoRow = width/strWidth;
        String[] splits = Pattern.compile("\\n").split(explanation);
        for(String split : splits) {
            Matcher row = Pattern.compile("[\\s\\S]{1,"+charsNoRow+"}").matcher(split);
            while(row.find()) {
                stringRow.add(row.group());
            }
        }
    }

    public void initialize(Pawn game) {

    }

    public int update() {
        return 0;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.8f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        batch.begin();
        for(int i = 0; i < stringRow.size; i++) {
            font.draw(batch,stringRow.get(i),x,y+2+strHeight*i);
        }
        batch.end();
    }

    public void dispose () {
    }

    public void setExplanation(String expl) {
        explanation = expl;
        stringRow.clear();
        String[] splits = Pattern.compile("\\n").split(explanation);
        for(String split : splits) {
            Matcher row = Pattern.compile("[\\s\\S]{1,"+charsNoRow+"}").matcher(split);
            while(row.find()) {
                stringRow.add(row.group());
            }
        }
    }
}
