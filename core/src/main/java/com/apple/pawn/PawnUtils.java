package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PawnUtils {

    /**
     * テキストを改行コードや実際の表示幅を用いて領域内に収めるよう改行させる
     *
     * @param text String 表示させるテキスト
     * @param width 領域の幅
     * @param fontCache 表示する際のフォントのキャッシュ
     * @return 改行に対応しつつ領域内に収まるように分割された文字列の配列
     */
    @NonNull
    public static Array<String> fontSplit(String text, int width, BitmapFontCache fontCache) {
        Array<String> fontSplits = new Array<>();
        String[] splits = Pattern.compile("\\n").split(text);
        for (String split : splits) {
            if(split.isEmpty()) split = " ";
            int i = 0;
            for(int j = 1; j <= split.length(); j++) {
                float fWidth = fontCache.setText(split.substring(i, j), 0, 0).width;
                if(fWidth > width) {
                    fontSplits.add(split.substring(i, j-1));
                    i = j - 1;
                }
            }
            fontSplits.add(split.substring(i));
        }
        return fontSplits;
    }

    /**
     * 引数の数字の中央値を求める
     *
     * @param values 中央値を求めたい数値群
     * @return 中央値
     */
    public static float median(float... values) {
        Arrays.sort(values);
        int size = values.length;
        int index = size >> 1;
        float target = values[index];
        if(size % 2 == 0) target = (values[index] + values[index - 1]) / 2;
        return target;
    }

    /**
     * X軸の中心の位置を指定して文字を描く
     * 
     * @see #fontDrawCenter(BitmapFont, SpriteBatch, String, float, float, boolean, boolean)
     */
    public static void fontDrawXCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float x, float y) {
        fontDrawCenter(font, batch, str, x, y, true, false);
    }

    public static void fontDrawXCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float start, float end, float y) {
        float x = (start + end) / 2;
        fontDrawCenter(font, batch, str, x, y, true, false);
    }

    /**
     * Y軸の中心の位置を指定して文字を描く
     *
     * @see #fontDrawCenter(BitmapFont, SpriteBatch, String, float, float, boolean, boolean)
     */
    public static void fontDrawYCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float x, float y) {
        fontDrawCenter(font, batch, str, x, y, false, true);
    }

    public static void fontDrawYCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float x, float start, float end) {
        float y = (start + end) / 2;
        fontDrawCenter(font, batch, str, x, y, false, true);
    }

    /**
     * X軸、Y軸の中心の位置を指定して文字を描く
     *
     * @see #fontDrawCenter(BitmapFont, SpriteBatch, String, float, float, boolean, boolean)
     */
    public static void fontDrawCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float x, float y) {
        fontDrawCenter(font, batch, str, x, y, true, true);
    }

    public static void fontDrawCenter(@NonNull BitmapFont font, SpriteBatch batch, String str, float xStart, float yStart, float xEnd, float yEnd) {
        float x = (xStart + xEnd) / 2;
        float y = (yStart + yEnd) / 2;
        fontDrawCenter(font, batch, str, x, y, true, true);
    }

    /**
     * X軸、Y軸の中心の位置を指定して文字を描く
     *
     * @param font 利用するフォント
     * @param batch 利用するSpriteBatch
     * @param str 描きたい文字列
     * @param x 中心に置きたいX座標
     * @param y 最下点(flipをtrueにしているならば最上点)のY座標
     * @param xCenter X軸の中心をとるか
     * @param yCenter Y軸の中心をとるか
     */
    public static void fontDrawCenter(@NonNull BitmapFont font,
                                      SpriteBatch batch,
                                      String str,
                                      float x,
                                      float y,
                                      boolean xCenter,
                                      boolean yCenter) {
        BitmapFontCache fontCache = font.getCache();
        int fixX = 0;
        int fixY = 0;
        if(xCenter) fixX = (int) (fontCache.setText(str, 0, 0).width / 2);
        if(yCenter) fixY = (int) (fontCache.setText(str, 0, 0).height / 2);
        font.draw(batch, str, x - fixX, y - fixY);
    }
}
