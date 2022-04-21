package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PawnUtils {

    /**
     * テキストを改行コードや実際の表示幅を用いて領域内に収めるよう改行させる
     *
     * @param text String 表示させるテキスト
     * @param width int 領域の幅
     * @param fontCache BitmapFontCache 表示する際のBitmapFontのBitmapFontCache
     * @return Array<String> 改行に対応しつつ領域内に収まるように分割されたテキスト
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
     * @param values float[] 中央値を求めたい数値たち
     * @return float 中央値
     */
    public static float median(float... values) {
        Arrays.sort(values);
        int size = values.length;
        int index = size >> 1;
        float target = values[index];
        if(size % 2 == 0) target = (values[index] + values[index - 1]) / 2;
        return target;
    }
}
