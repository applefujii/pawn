package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Pattern;

public class FontUtils {
    public static Array<String> fontSplit(String text, int width, BitmapFontCache fontCache) {
        Array<String> fontSplits = new Array<>();
        String[] splits = Pattern.compile("\\n").split(text);
        for (String split : splits) {
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
}
