package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

public class BoardSurface {
    public static final int MAP_WIDTH = 4096, MAP_HEIGHT = 4096;
    public static final int BACK_HEIGHT = 2700;
    public static final Array<Vector2> MAP_COORDINATE;
    private static final String MAP_DATA_NAME[] = {
            "map.jsonc",
            "map2.jsonc",
            "map3.jsonc"
    };

    private Sprite backSprite;
    private final Array<Square> aSquare;
    private final Vector2 cameraPos;

    static {
        MAP_COORDINATE = new Array<>();
        int i;
        int j;
        for(j = 0; j < 16; j++) {
            for(i = 0; i < 16; i++) {
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

        backSprite = new Sprite(manager.get("assets/background.png", Texture.class));
        backSprite.flip(false, true);
        backSprite.setScale((float) (MAP_HEIGHT + Pawn.LOGICAL_HEIGHT) / BACK_HEIGHT);
        backSprite.setCenter(MAP_WIDTH >> 1, MAP_HEIGHT >> 1);
        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.initialize(manager, aSquare.size - 1, font);
        }
    }

    public void update(GameScreen gameScreen) {
        if(FlagManagement.is(Flag.LOOK_FREE)) {
            cameraPos.set(gameScreen.getCameraPos().x, gameScreen.getCameraPos().y);
            Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
            while (squareIterator.hasNext()) {
                Square square = squareIterator.next();
                 square.update(gameScreen, cameraPos);
            }
        }
    }

    public void draw (SpriteBatch batch, ShapeRenderer renderer) {
        batch.begin();
        batch.disableBlending();

        backSprite.draw(batch);

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch);
        }

        batch.enableBlending();

        squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.drawFont(batch);
        }

        batch.end();
    }

    public void dispose () {
//        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
//        while(squareIterator.hasNext()) {
//            Square square = squareIterator.next();
//            square.dispose();
//        }
    }

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
