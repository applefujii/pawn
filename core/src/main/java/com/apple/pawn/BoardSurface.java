package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

public class BoardSurface {
    public static final int MAP_WIDTH = 4096, MAP_HEIGHT = 4096;
    public static final int BACK_HEIGHT = 2700;
    public static final int SQUARE_COUNT = 65;
    public static final Array<Vector2> MAP_COORDINATE;

    private final Texture backImg;
    private final Sprite backSprite;
    private final Array<Square> aSquare;
    private final TextureAtlas squareAtlas;

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
        backImg = new Texture(Gdx.files.local("assets/background.png"));
        backSprite = new Sprite(backImg);
        backSprite.flip(false, true);
        backSprite.setScale((float) MAP_HEIGHT / BACK_HEIGHT);
        backSprite.setCenter(MAP_WIDTH >> 1, MAP_HEIGHT >> 1);
        squareAtlas = new TextureAtlas(Gdx.files.local("assets/map_atlas.txt"));
        aSquare = new Array<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode aSquareJson = objectMapper.readTree(Gdx.files.local("assets/a_square_json.jsonc").file());
            int count = 0;
            for(JsonNode squareJson : aSquareJson) {
                int add = squareJson.get("address").asInt();
                int type = squareJson.get("type").asInt();
                if(type == 4) aSquare.add(new TaskSquare(MAP_COORDINATE.get(add), type, count, squareJson.get("document").asText()));
                else if(type == 3) aSquare.add(new EventSquare(MAP_COORDINATE.get(add), type, count, squareJson.get("document").asText()));
                else aSquare.add(new Square(MAP_COORDINATE.get(add), type, count));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.initialize(squareAtlas);
        }
    }

    public void update() { }

    public void draw (Batch batch, ShapeRenderer renderer) {
        //仮のマップ背景描写
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(0, 1, 0, 1);
//        renderer.rect(0, 0, MAP_WIDTH, MAP_HEIGHT);
//        renderer.end();

        batch.begin();

        backSprite.draw(batch);

        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
        while(squareIterator.hasNext()) {
            Square square = squareIterator.next();
            square.draw(batch);
        }

        batch.end();
    }

    public void dispose () {
//        Iterator<Square> squareIterator = new Array.ArrayIterator<>(aSquare);
//        while(squareIterator.hasNext()) {
//            Square square = squareIterator.next();
//            square.dispose();
//        }
//        mapImg.dispose();
        backImg.dispose();
        squareAtlas.dispose();
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
