package com.apple.pawn;

import static com.apple.pawn.PawnUtils.fontSplit;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class TaskSquare extends Square {
    private final String doc;
    private Array<String> drawDoc;

    public TaskSquare(Vector2 coo, int type, int count, String document, int move, int back) {
        super(coo, type, count);
        this.move = move;
        this.back = back;
        doc = document;
        drawDoc = new Array<>();
    }

    @Override
    public void initialize(@NonNull AssetManager manager, int size, BitmapFont font) {
        super.initialize(manager, size, font);
        BitmapFontCache fontCache = font.getCache();
        move = Math.min(move, size - count);
        back = Math.min(back, count);
        document = doc + "\n\n成功で" + move + "マス進む\n失敗で" + back + "マス戻る";
        uiDoc = uiDoc + "\n\n" + document;
        if(Objects.nonNull(doc)) {
            int charsNoCol = (SQUARE_HEIGHT - 32) / 18;
            drawDoc = fontSplit(doc, SQUARE_WIDTH - 32, fontCache);
            if (drawDoc.size > charsNoCol - 3) {
                drawDoc.setSize(charsNoCol - 4);
                String peek = drawDoc.peek();
                int i = peek.length() - 1;
                while(i > 0) {
                    float fWidth = fontCache.setText(peek.substring(0, i) + "...", 0, 0).width;
                    if(fWidth <= SQUARE_WIDTH - 32) break;
                    i--;
                }
                drawDoc.insert(charsNoCol - 3, peek.substring(0, i) + "...");
            }
        }
        drawDoc.add("", "成功で"+move+"マス進む", "失敗で"+back+"マス戻る");
    }

    @Override
    public void drawFont(SpriteBatch batch) {
        if(!FlagManagement.is(Flag.LOOK_MAP)) {
            for (int i = 0; i < drawDoc.size; i++) {
                font.draw(batch, drawDoc.get(i), position.x + 16, position.y + 16 + (18 * i));
            }
        }
    }
}
