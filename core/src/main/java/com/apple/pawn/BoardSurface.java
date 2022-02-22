package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class BoardSurface {
    public static int TILE_LENGTH = 256;

    private final Array<Piece> aPiece;
    private final Array<Square> aSquare;
    private final Array<Array<Integer>> mapAddress;
    private final Array<Integer> squareAddress;
    private final TextureAtlas mapAtlas;

    private final Sprite backSprite;

    public BoardSurface() {
        mapAtlas = new TextureAtlas(Gdx.files.internal("map_atlas.txt"));
        backSprite = mapAtlas.createSprite("back");
        backSprite.flip(false, true);
        aPiece = new Array<>();
        aSquare = new Array<>();

        mapAddress = new Array<>();
        int i;
        int j;
        for(j = 0; j < 16; j++) {
            for(i = 0; i < 16; i++) {
                Array<Integer> address = new Array<>();
                address.add(i, j);
                mapAddress.add(address);
            }
        }

        squareAddress = new Array<>();
        for(j = 0; j < 192; j +=64) {
            for(i = 35; i < 45; i++) squareAddress.add(i + j);
            squareAddress.add(60 + j);
            for(i = 76; i > 66; i--) squareAddress.add(i + j);
            squareAddress.add(83 + j);
        }

        initialize();
    }

    private void initialize() {
        Piece pi = new Piece("1P");
        pi.setSquareNo(0);
        aPiece.add( pi );
        pi = new Piece("2P");
        pi.setSquareNo(1);
        aPiece.add( pi );

        boolean start = true;
        Array.ArrayIterator<Integer> ite = new Array.ArrayIterator<>(squareAddress);
        while(ite.hasNext()) {
            int index = ite.next();
            if(start) {
                aSquare.add(new Square(mapAddress.get(index).get(0), mapAddress.get(index).get(1), 0, mapAtlas));
                start = false;
            }
            else if(!ite.hasNext()) aSquare.add(new Square(mapAddress.get(index).get(0), mapAddress.get(index).get(1), 1, mapAtlas));
            else aSquare.add(new Square(mapAddress.get(index).get(0), mapAddress.get(index).get(1), MathUtils.random(2, 3), mapAtlas));
        }
    }

    public void update() {
        Iterator<Piece> pieceIterator = new Array.ArrayIterator<>(aPiece);
        while(pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            piece.update();
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();

        backSprite.setSize(TILE_LENGTH, TILE_LENGTH);
        for(int i = 0; i < 256; i++) {
            if(squareAddress.contains(i, true)) continue;
            backSprite.setPosition(TILE_LENGTH * mapAddress.get(i).get(0), TILE_LENGTH * mapAddress.get(i).get(1));
            backSprite.draw(batch);
        }

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch);
        }

        batch.end();

        Iterator<Piece> pieceIterator = new Array.ArrayIterator<>(aPiece);
        while(pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            piece.draw(batch, renderer);
        }
    }

    public void dispose () {
        mapAtlas.dispose();
    }

}
