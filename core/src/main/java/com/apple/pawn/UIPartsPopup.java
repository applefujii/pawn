package com.apple.pawn;

import static com.apple.pawn.PawnUtils.fontSplit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class UIPartsPopup extends UIParts{

    private final Array<String> stringRow;
    private final int strHeight = 18;
    private float time_show;
    private float time_fadein = 0.5f, time_fadeout = 0.5f;
    private float timer = 0;
    private float alpha = 0;
    private int state = 0;      // 0:fadein 1:show 2:fadeout 3:end

    protected Sprite sprite;

    public UIPartsPopup(String name, AssetManager manager, BitmapFont font, int x, int y, int width, int height, int group, String text, float time_show) {
        super(name, x, y, width, height, group);
        this.time_show = time_show;
        stringRow = fontSplit(text, width - (px * 2), font.getCache());
        sprite = manager.get("assets/ui_atlas.txt", TextureAtlas.class).createSprite("popup");
        sprite.flip(false, true);
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
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(0.8f,0.8f,0.8f,alpha);
//        renderer.box(x,y,0,width,height,0);
//        renderer.end();

        batch.begin();
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        sprite.setAlpha(alpha);
        sprite.draw(batch);
        Color color = font.getColor();
        color.a = alpha;
        font.setColor(color);
        for(int i = 0; i < stringRow.size; i++) {
            font.draw(batch,stringRow.get(i),x+px,y+(height>>1)-(strHeight*stringRow.size>>1)+2+strHeight*i);
        }
        color.a = 1.0f;
        font.setColor(color);
        batch.end();
    }

    @Override
    public void dispose() { }

}
