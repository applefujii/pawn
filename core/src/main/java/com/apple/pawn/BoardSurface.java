package com.apple.pawn;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class BoardSurface {
    public static int MAP_WIDTH = 2048;
    private Array<Piece> aPiece;
    private Array<Square> aSquare;

    private Texture img;        // テクスチャ
    private Group group;

    public BoardSurface() {
        Pixmap mapPix = new Pixmap(MAP_WIDTH, MAP_WIDTH, Pixmap.Format.RGB888);
        mapPix.setColor(0, 1, 0, 1);
        mapPix.fill();
        img = new Texture(mapPix);
        mapPix.dispose();
        aPiece = new Array<Piece>();
        aSquare = new Array<Square>();
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
        Square sq = new Square(x, y);
        aSquare.add(sq);
    }

    public void update() {
        for(Piece pi : aPiece) {
            pi.update();
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(MAP_WIDTH, MAP_WIDTH);
//        sprite.setRegion(100,100,100,100);
        sprite.setPosition(0, 0);   // ※仮
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
        batch.end();

        for(Square sq : aSquare) {
            sq.draw(batch, renderer);
        }

        for(Piece pi : aPiece) {
            pi.draw(batch, renderer);
        }
    }

    public void dispose () {
        img.dispose();
    }

}
