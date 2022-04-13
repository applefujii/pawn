package com.apple.pawn;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskSquare extends Square {
    private final String doc;
    private final Array<String> drawDoc;

    public TaskSquare(Vector2 coo, int type, int count, String document, int move, int back) {
        super(coo, type, count);
        this.move = move;
        this.back = back;
        doc = document;
        drawDoc = new Array<>();
    }

    @Override
    public void initialize(AssetManager manager, int size, BitmapFont font) {
        super.initialize(manager, size, font);
        move = Math.min(move, size - count);
        back = Math.min(back, count);
        document = doc + "\n成功で" + move + "マス進む\n失敗で" + back + "マス戻る";
        uiDoc = uiDoc + "\n" + document;
        if(Objects.nonNull(doc)) {
            int charsNoRow = (SQUARE_WIDTH - 32) / 18;
            int charsNoCol = (SQUARE_HEIGHT - 32) / 18;
            String[] splits = Pattern.compile("\\n").split(doc);
            for (String split : splits) {
                Matcher row = Pattern.compile("[\\s\\S]{1," + charsNoRow + "}").matcher(split);
                while (row.find()) {
                    drawDoc.add(row.group());
                }
            }
            if (drawDoc.size > charsNoCol - 2) {
                drawDoc.setSize(charsNoCol - 2);
                String peek = drawDoc.peek();
                if (peek.length() >= charsNoRow) peek = peek.replaceFirst(".$", "...");
                else peek += "...";
                drawDoc.insert(charsNoCol - 3, peek);
            }
        }
        drawDoc.add("成功で"+move+"マス進む", "失敗で"+back+"マス戻る");
    }

    @Override
    public void drawFont(SpriteBatch batch) {
        for(int i = 0; i < drawDoc.size; i++) {
            font.draw(batch, drawDoc.get(i), position.x + 16, position.y + 16 + (18 * i));
        }
    }
}
