package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class BoardSurface {
    private Array<Piece> aPiece;
    private Array<Square> aSquare;

    private Texture img;        // テクスチャ
    private Group group;

    public BoardSurface() {
        img = new Texture("badlogic.jpg");
        aPiece = new Array<Piece>();
        aSquare = new Array<Square>();
        group = new Group();
        Initialize();
    }

    private void  Initialize() {
        Piece pi = new Piece("1P");
        pi.setSquareNo(0);
        aPiece.add( pi );
        pi = new Piece("2P");
        pi.setSquareNo(1);
        aPiece.add( pi );
    }

    public void update() {
        for(Piece pi : aPiece) {
            pi.update();
        }
    }

    public void draw (Batch batch) {
        batch.begin();
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(2000, 2000);
//        sprite.setRegion(100,100,100,100);
        sprite.setPosition(0, 0);   // ※仮
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
        batch.end();

        for(Piece pi : aPiece) {
            pi.draw(batch);
        }
    }

    public void dispose () {
        img.dispose();
    }

}
