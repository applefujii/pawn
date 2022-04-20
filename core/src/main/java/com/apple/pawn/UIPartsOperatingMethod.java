package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.regex.Pattern;

public class UIPartsOperatingMethod extends UIParts{
    private String[] documents;

    public UIPartsOperatingMethod(String name, String document) {
        super(name, 0, 0, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
        documents = Pattern.compile("\\n").split(document);
    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public void draw(Batch batch, ShapeRenderer renderer, BitmapFont font) {
        batch.begin();
        for(int i = 0; i < documents.length; i++) {
            font.draw(batch, documents[i], px, Pawn.LOGICAL_HEIGHT - py - (18 * (documents.length - i)));
        }
        batch.end();
    }

    @Override
    public void dispose() { }

    public void setDocument(String document) {
        documents = Pattern.compile("\\n").split(document);
    }

    public void clear() {
        if(documents.length > 0) documents = new String[]{};
    }
}