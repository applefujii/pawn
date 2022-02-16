package com.apple.pawn;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class BoardSurface {
    public static int MAP_LENGTH = 2048;
    private final Array<Piece> aPiece;
    private final Array<Square> aSquare;

    private final Texture img;        // テクスチャ
    private final Group group;

    public BoardSurface() {
        Pixmap mapPix = new Pixmap(MAP_LENGTH, MAP_LENGTH, Pixmap.Format.RGB888);
        mapPix.setColor(0, 1, 0, 1);
        mapPix.fill();
        img = new Texture(mapPix);
        mapPix.dispose();
        aPiece = new Array<>();
        aSquare = new Array<>();
        group = new Group();
        initialize();
    }

    private void initialize() {
        Piece pi = new Piece("1P");
        pi.setSquareNo(0);
        aPiece.add( pi );
        pi = new Piece("2P");
        pi.setSquareNo(1);
        aPiece.add( pi );

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
        Iterator<Piece> pieceIterator = new Array.ArrayIterator<>(aPiece);
        while(pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            piece.update();
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(MAP_LENGTH, MAP_LENGTH);
//        sprite.setRegion(100,100,100,100);
        sprite.setPosition(0, 0);   // ※仮
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
        batch.end();

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch, renderer);
        }

        Iterator<Piece> pieceIterator = new Array.ArrayIterator<>(aPiece);
        while(pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            piece.draw(batch, renderer);
        }
    }

    public void dispose () {
        img.dispose();
    }

}
