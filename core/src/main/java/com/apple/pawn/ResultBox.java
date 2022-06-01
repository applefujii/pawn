package com.apple.pawn;

import static com.apple.pawn.PawnUtils.fontDrawCenter;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ResultBox {
    public static float WIDTH = 0;
    public static float HEIGHT = 0;

    final Array<Float> xs;
    final float y;
    final float xCount;
    final float rowWidth;
    final float yCount;
    final Array<Float> xCenters;
    final float yCenter;
    final float spriteWidth;
    final float spriteHeight;
    final boolean isXCenter;
    final boolean isYCenter;
    final int divide;
    final Array<String> texts;
    final Array<Sprite> sprites;
    final boolean hasText;
    final boolean hasSprite;

    private final Array<Float> dx;
    private final float dy;

    public ResultBox(float x, float y, float xCount, float yCount) {
        this(x, y, xCount, yCount, 0, 0, false, false, 1, new Array<>(), new Array<>());
    }

    public ResultBox(float x, float y, float xCount, float yCount, boolean isXCenter, boolean isYCenter, String text) {
        this(x, y, xCount, yCount, isXCenter, isYCenter, 1, text);
    }

    public ResultBox(float x, float y, float xCount, float yCount, boolean isXCenter, boolean isYCenter, int divide, String... texts) {
        this(x, y, xCount, yCount, 0, 0, isXCenter, isYCenter, divide, new Array<>(texts), new Array<>());
    }

    public ResultBox(float x, float y, float xCount, float yCount, float spriteWidth, float spriteHeight, Sprite sprite) {
        this(x, y, xCount, yCount, spriteWidth, spriteHeight, 1, sprite);
    }

    public ResultBox(float x, float y, float xCount, float yCount, float spriteWidth, float spriteHeight, int divide, Sprite... sprites) {
        this(x, y, xCount, yCount, spriteWidth, spriteHeight, true, true, divide, new Array<>(), new Array<>(sprites));
    }

    public ResultBox(float x, float y, float xCount, float yCount, float spriteWidth, float spriteHeight, int divide, Array<String> texts, Array<Sprite> sprites) {
        this(x, y, xCount, yCount, spriteWidth, spriteHeight, true, true, divide, texts, sprites);
    }

    private ResultBox(float x,
                      float y,
                      float xCount,
                      float yCount,
                      float spriteWidth,
                      float spriteHeight,
                      boolean isXCenter,
                      boolean isYCenter,
                      int divide,
                      @NonNull Array<String> texts,
                      @NonNull Array<Sprite> sprites) {
        xs = new Array<>();
        this.y = y;
        this.xCount = xCount;
        rowWidth = (WIDTH * xCount) / divide;
        this.yCount = yCount;
        xCenters = new Array<>();
        yCenter = y + ((HEIGHT * yCount) / 2);
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.isXCenter = isXCenter;
        this.isYCenter = isYCenter;
        this.divide = divide;
        this.texts = texts;
        this.sprites = sprites;
        hasText = texts.notEmpty();
        hasSprite = sprites.notEmpty();
        for(int i = 0; i < divide; i++) {
            xs.add(x + (rowWidth * i));
            xCenters.add(x + (rowWidth / 2) + (rowWidth * i));
        }
        if (isXCenter) dx = xCenters;
        else dx = xs;
        if (isYCenter) dy = yCenter;
        else dy = y;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < divide; i++) {
            if (hasSprite) {
                Sprite sp = sprites.get(i);
                sp.setSize(spriteWidth, spriteHeight);
                sp.setCenter(dx.get(i).intValue(), (int) dy);
                sp.draw(batch);
            }
            if (hasText) fontDrawCenter(font, batch, texts.get(i), dx.get(i), dy, isXCenter, isYCenter);
        }
    }
}
