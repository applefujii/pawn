package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BoardSurface {
    public static final int MAP_WIDTH = 4096, MAP_HEIGHT = 4096;
    public static final Array<Vector2> MAP_COORDINATE;
    private static final String[] MAP_DATA_NAME = {
            "map.jsonc",
            "map2.jsonc",
            "map3.jsonc"
    };

    private Sprite backSprite;
    private final Array<Square> aSquare;
    private final Vector2 cameraPos;

    static {
        MAP_COORDINATE = new Array<>();
        for(int j = 0; j < 16; j++) {
            for(int i = 0; i < 16; i++) {
                Vector2 coo = new Vector2(i, j);
                MAP_COORDINATE.add(coo);
            }
        }
    }

    public BoardSurface() {
        aSquare = new Array<>();
        cameraPos = new Vector2();
    }

    public void initialize(AssetManager manager, int mapNo, BitmapFont font) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode mapJson = objectMapper.readTree(Gdx.files.local("assets/"+ MAP_DATA_NAME[mapNo]).file());
            int count = 0;
            for(JsonNode mJ : mapJson) {
                int add = mJ.path("address").asInt();
                int type = mJ.path("type").asInt();
                if(type == 4) aSquare.add(new TaskSquare(MAP_COORDINATE.get(add).cpy(), type, count, mJ.path("document").asText(), mJ.path("move").asInt(), mJ.path("back").asInt()));
                else if(type == 3) aSquare.add(new EventSquare(MAP_COORDINATE.get(add).cpy(), type, count, mJ.path("move").asInt()));
                else aSquare.add(new Square(MAP_COORDINATE.get(add).cpy(), type, count));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        backSprite = new Sprite(manager.get("assets/back.png", Texture.class));
        backSprite.flip(false, true);
        for(int i = 0; i < aSquare.size; i++) aSquare.get(i).initialize(manager, aSquare.size - 1, i, font);
    }

    public void update(GameScreen gameScreen) {
        if(FlagManagement.is(Flag.LOOK_FREE)) {
            cameraPos.set(gameScreen.getCameraPos().x, gameScreen.getCameraPos().y);
            for(Square square : aSquare) square.update(gameScreen, cameraPos);
        }
    }

    public void draw (@NonNull SpriteBatch batch) {
        batch.begin();
        batch.disableBlending();

        for(int j = 0; j < 47; j++) {
            for(int i = 0; i < 47; i++) {
                backSprite.setPosition(i * 256 - MAP_WIDTH, j * 256 - MAP_HEIGHT);
                backSprite.draw(batch);
            }
        }

        for(Square square : aSquare) square.draw(batch);

        batch.enableBlending();

        for(Square square : aSquare) square.drawFont(batch);

        batch.end();
    }

    public void dispose () { }

    public Square getSquare(int squareNo) {
        return aSquare.get(squareNo);
    }

    public Vector2 getPos(int squareNo) {
        Square s;
        if(squareNo <= aSquare.size-1) s = aSquare.get(squareNo);
        else s = aSquare.peek();
        return s.getPos();
    }

    public int getSquareCount() {
        return aSquare.size;
    }
}
