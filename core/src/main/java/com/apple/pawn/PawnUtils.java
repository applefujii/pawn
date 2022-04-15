package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Pattern;

public class PawnUtils {
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
     * 三つの値の中央値を求めるメソッド
     *
     * @param a float
     * @param b float
     * @param c float
     * @return a,b,cの中央値float
     */
    public static float median(float a, float b, float c) {
        int n = 0;
        if(a < b) n++;
        else if(a == b) return a;
        if(b > c) n++;
        else if(b == c) return b;
        //(a<b<c)または(a>b>c)の場合
        if(n == 1) return b;
            //(a>b<c, n=0)または(a<b>c, n=2)の場合
        else if(n == 0) return Math.min(a, c);
        else return Math.max(a, c);
    }
}
