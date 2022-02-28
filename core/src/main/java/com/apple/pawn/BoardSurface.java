package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class BoardSurface {
    public static int TILE_LENGTH = 256;
    public static int SQUARE_COUNT = 65;
    public static Array<Array<Integer>> MAP_ADDRESS;
    public static Array<Integer> SQUARE_NO;

    private static int i;

    private final Array<Square> aSquare;
    private final TextureAtlas mapAtlas;

    private final Sprite backSprite;

    static {
        MAP_ADDRESS = new Array<>();
        int j;
        for(j = 0; j < 16; j++) {
            for(i = 0; i < 16; i++) {
                Array<Integer> address = new Array<>();
                address.add(i, j);
                MAP_ADDRESS.add(address);
            }
        }

        SQUARE_NO = new Array<>();
        for(j = 0; j < 192; j +=64) {
            for(i = 35; i < 45; i++) SQUARE_NO.add(i + j);
            SQUARE_NO.add(60 + j);
            for(i = 76; i > 66; i--) SQUARE_NO.add(i + j);
            SQUARE_NO.add(83 + j);
        }
    }

    public BoardSurface() {
        mapAtlas = new TextureAtlas(Gdx.files.internal("map_atlas.txt"));
        backSprite = mapAtlas.createSprite("back");
        backSprite.flip(false, true);
        aSquare = new Array<>();

        initialize();
    }

    private void initialize() {
        boolean start = true;
        int count = 0;
        Array.ArrayIterator<Integer> ite = new Array.ArrayIterator<>(SQUARE_NO);
        while(ite.hasNext()) {
            int index = ite.next();
            int type = MathUtils.random(2, 3);
            if(start) {
                aSquare.add(new Square(MAP_ADDRESS.get(index).get(0), MAP_ADDRESS.get(index).get(1), 0, count, mapAtlas));
                start = false;
            }
            else if(!ite.hasNext()) aSquare.add(new Square(MAP_ADDRESS.get(index).get(0), MAP_ADDRESS.get(index).get(1), 1, count, mapAtlas));
            else if(type == 3 && EventSquare.DOCUMENTS.notEmpty()) aSquare.add(new EventSquare(MAP_ADDRESS.get(index).get(0), MAP_ADDRESS.get(index).get(1), 3, count, mapAtlas));
            else aSquare.add(new Square(MAP_ADDRESS.get(index).get(0), MAP_ADDRESS.get(index).get(1), 2, count, mapAtlas));
            count++;
        }
    }

    public void update() {
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();

        backSprite.setSize(TILE_LENGTH, TILE_LENGTH);
        for(i = 0; i < 256; i++) {
            if(SQUARE_NO.contains(i, true)) continue;
            backSprite.setPosition(TILE_LENGTH * MAP_ADDRESS.get(i).get(0), TILE_LENGTH * MAP_ADDRESS.get(i).get(1));
            backSprite.draw(batch);
        }

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch);
        }

        batch.end();
    }

    public void dispose () {
        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.dispose();
        }
        mapAtlas.dispose();
    }

    public Square getSquare(int squareCount) {
        return this.aSquare.get(squareCount);
    }

    public Vector2 getPos(int squareNo) {
        Square s;
        if(squareNo <= aSquare.size-1) s = aSquare.get(squareNo);
        else s = aSquare.peek();
        return new Vector2(BoardSurface.TILE_LENGTH*s.x, BoardSurface.TILE_LENGTH*s.y);
    }

    public int getSquareCount() {
        return aSquare.size;
    }
}
