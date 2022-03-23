package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIPartsPopup extends UIParts{

    private String text;
    private final Array<String> stringRow;
    private final int charsNoRow;
    private final int strWidth = 18;
    private final int strHeight = 18;
    private float time_show;
    private float time_fadein = 0.5f, time_fadeout = 0.5f;
    private float timer = 0;
    private float alpha = 0;
    private int state = 0;      // 0:fadein 1:show 2:fadeout 3:end

    public UIPartsPopup(String name, int x, int y, int width, int height, String text, float time_show) {
        super(name, x, y, width, height);
        this.text = text;
        this.time_show = time_show;
        stringRow = new Array<>();
        charsNoRow = (width-px/2)/strWidth;
        String[] splits = Pattern.compile("\\n").split(text);
        for(String split : splits) {
            Matcher row = Pattern.compile("[\\s\\S]{1,"+charsNoRow+"}").matcher(split);
            while(row.find()) {
                stringRow.add(row.group());
            }
        }
    }

    @Override
    public int update() {
        timer += Gdx.graphics.getDeltaTime();
        float time = 0;
        if(state == 0) time = time_fadein;
        else if(state == 1) time = time_show;
        else if(state == 2) time = time_fadeout;
        //-- 時間が来ていたら次のstateに進める
        if(timer >= time) {
            timer = 0;
            state++;
            if(state == 0) time = time_fadein;
            else if(state == 1) time = time_show;
            else if(state == 2) time = time_fadeout;
        }

        switch(state) {
            case 0:
                alpha = (float)Math.pow(timer/time, 3);
                break;
            case 1:
                alpha = 1;
                break;
            case 2:
                alpha = (float)Math.pow(1-timer/time, 3);
                break;
        }

        if(state == 3) return -1;
        return 0;
    }

    @Override
    public void draw(Batch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.8f,0.8f,0.8f,alpha);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        batch.begin();
        Color color = font.getColor();
        color.a = alpha;
        font.setColor(color);
        for(int i = 0; i < stringRow.size; i++) {
            font.draw(batch,stringRow.get(i),x+px,y+(height/2)-(strHeight*stringRow.size/2)+2+strHeight*i);
        }
        color.a = 1.0f;
        font.setColor(color);
        batch.end();
    }

    @Override
    public void dispose() {

    }

}