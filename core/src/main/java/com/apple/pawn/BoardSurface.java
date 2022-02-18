package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class BoardSurface {
    public static int MAP_LENGTH = 2048;
    public static int TILE_LENGTH = 256;
    public static TextureAtlas mapAtlas = new TextureAtlas(Gdx.files.internal("map_atlas.txt"));
    private final Array<Square> aSquare;

    //-- 参照
//    private final Array<Piece> aPiece;

    private final Sprite backSprite;
    private final Group group;

    public BoardSurface() {
        backSprite = mapAtlas.createSprite("back");
        backSprite.flip(false, true);
        aSquare = new Array<>();
        group = new Group();
        initialize();
    }

    private void initialize() {
        int i;
        int j;
        for(i = 0; i <= 4; i += 4) {
            for(j = 0; j < 8; j++) newSquare(j, i);
            newSquare(7, 1+i);
            for(j = 7; j >= 0; j--) newSquare(j, 2+i);
            newSquare(0, 3+i);
        }
    }

    private void newSquare(int x, int y) {
        Square square = new Square(x, y);
        aSquare.add(square);
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        backSprite.setSize(MAP_LENGTH, MAP_LENGTH);
        backSprite.setPosition(0, 0);
        backSprite.draw(batch);
        batch.end();

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch, renderer);
        }
    }

    public void dispose () {
        mapAtlas.dispose();
    }

}
