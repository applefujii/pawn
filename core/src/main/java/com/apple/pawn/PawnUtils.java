package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PawnUtils {

    /**
     *
     * @param text String 表示させるテキスト
     * @param width int 表示させる幅
     * @param fontCache BitmapFontCache 表示する際のBitmapFontのBitmapFontCache
     * @return Array<String> 改行に対応しつつ表示幅内に収まるように分割されたテキスト
     */
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
                    i = j;
                }
            }
            fontSplits.add(split.substring(i));
        }
        return fontSplits;
    }

    /**
     * 引数の数字の中央値を求める
     *
     * @param values float[] 中央値を求めたい数値たち
     * @return float 中央値
     */
    public static float median(float... values) {
        Arrays.sort(values);
        int size = values.length;
        int index = (int) Math.floor((double) size / 2);
        float target = values[index];
        if(size % 2 == 0) target = (values[index] + values[index - 1]) / 2;
        return target;
    }
}
