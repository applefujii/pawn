package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square {
    protected int type;               // マスの種類
    protected String document;        // マスの文字

    public Square( String doc ) {
        document = doc;
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
    }

    public void dispose () {
    }
}
