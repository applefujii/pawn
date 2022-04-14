package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fujii
 */
public class UIPartsExplanation extends UIParts {

    private Array<String> stringRow;
    private final int strHeight = 18;
    private final BitmapFontCache fontCache;

    protected Sprite sprite;

    public UIPartsExplanation(String name, AssetManager manager, BitmapFont font, int x, int y, int width, int height, String expl) {
        super(name, x, y, width, height);
        stringRow = new Array<>();
        fontCache = font.getCache();
        sprite = manager.get("assets/ui_atlas.txt", TextureAtlas.class).createSprite("explanation");
        sprite.flip(false, true);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        setExplanation(expl);
    }

    public void initialize(Pawn game) { }

    public int update() {
        return 0;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(0.8f,0.8f,0.8f,1);
//        renderer.box(x,y,0,width,height,0);
//        renderer.end();

        batch.begin();
        sprite.draw(batch);
        for(int i = 0; i < stringRow.size; i++) {
            font.draw(batch,stringRow.get(i),x+px,y+py+2+strHeight*i);
        }
        batch.end();
    }

    public void dispose () { }

    public void setExplanation(String expl) {
        stringRow = FontUtils.fontSplit(expl, width - (px * 2), fontCache);
    }
}
