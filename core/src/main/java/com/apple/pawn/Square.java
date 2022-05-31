package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import java.util.Objects;

public class Square {
    public static final String[] TYPE_STR = {"start", "goal", "normal", "event", "task"};
    public static final String[] TYPE_STR_JP = {"スタート", "ゴール", "通常マス", "イベントマス", "課題マス"};
    public static final int SQUARE_WIDTH = 256, SQUARE_HEIGHT = 256;

    protected final Vector2 coordinate;
    protected final Vector2 position;
    protected final int type;
    protected final int count;

    protected Sprite sprite;
    protected String document;
    protected String uiDoc;
    protected int move;
    protected int back;
    protected BitmapFont font;
    protected int no;

    //-- 参照
    protected Array<Piece> aPiece;

    public Square(Vector2 coo, int type, int count) {
        coordinate = coo;
        position = new Vector2(SQUARE_WIDTH*coordinate.x, SQUARE_HEIGHT*coordinate.y);
        this.type = type;
        this.count = count;
        aPiece = new Array<>();
    }

    public void initialize(@NonNull AssetManager manager, int size, int no, BitmapFont font) {
        this.no = no;
        sprite = manager.get("map_atlas.txt", TextureAtlas.class).createSprite(TYPE_STR[type]);
        sprite.flip(false, true);
        sprite.setScale((float) SQUARE_WIDTH / sprite.getWidth(), (float) SQUARE_HEIGHT / sprite.getHeight());
        sprite.setPosition(position.x, position.y);
        this.font = font;
        uiDoc = TYPE_STR_JP[type];
    }

    public void update(GameScreen gameScreen, Vector2 cameraPos) {
        if(sprite.getBoundingRectangle().contains(cameraPos)) {
            gameScreen.setUIPartsExplanation(uiDoc);
        }
    }

    public void draw (SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void drawFont(SpriteBatch batch) { }

    public void dispose () { }

    public void addPiece(Piece piece) {
        aPiece.add(piece);
    }

    public void removePiece(Piece piece) {
        aPiece.removeValue(piece, false);
    }

    public int getNo() {
        return no;
    }

    public Vector2 getPos() {
        return position.cpy();
    }

    public boolean hasDocument() {
        return Objects.nonNull(document);
    }

    public @Null String getDocument() {
        return document;
    }

    public int getType() {
        return type;
    }

    public int getMove() {
        return move;
    }

    public int getBack() {
        return back;
    }
}
