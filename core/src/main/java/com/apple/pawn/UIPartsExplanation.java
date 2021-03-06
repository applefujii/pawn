package com.apple.pawn;

import static com.apple.pawn.PawnUtils.fontSplit;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * 説明表示UI
 * @author fujii
 */
public class UIPartsExplanation extends UIParts {

    private Array<String> stringRow;
    private String explanation;

    private final BitmapFontCache fontCache;

    protected Sprite sprite;

    public UIPartsExplanation(String name, @NonNull AssetManager manager, @NonNull BitmapFont font, int x, int y, int width, int height, int group, String expl) {
        super(name, x, y, width, height, group);
        stringRow = new Array<>();
        explanation = "";
        fontCache = font.getCache();
        sprite = manager.get("ui_atlas.txt", TextureAtlas.class).createSprite("explanation");
        sprite.flip(false, true);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        setExplanation(expl);
    }

    public void initialize(Pawn game) { }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public void draw (@NonNull SpriteBatch batch, ShapeRenderer renderer, @NonNull BitmapFont font) {
        batch.begin();
        sprite.draw(batch);
        for(int i = 0; i < stringRow.size; i++) {
            font.draw(batch,stringRow.get(i),x+px,y+py+(20*i));
        }
        batch.end();
    }

    @Override
    public void dispose () { }

    public void setExplanation(String expl) {
        if(!explanation.equals(expl)) {
            explanation = expl;
            stringRow = fontSplit(expl, width - (px * 2), fontCache);
        }
    }
}
