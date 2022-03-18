package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

public class BoardSurface {
    public static final int MAP_WIDTH = 4096, MAP_HEIGHT = 4096;
    public static final int TILE_WIDTH = 256, TILE_HEIGHT = 256;
    public static final int SQUARE_COUNT = 65;
    public static final Array<Vector2> MAP_ADDRESS;

    private final Array<Square> aSquare;

    private final Texture mapImg;

    static {
        MAP_ADDRESS = new Array<>();
        int i;
        int j;
        for(j = 0; j < 16; j++) {
            for(i = 0; i < 16; i++) {
                Vector2 pos = new Vector2(i, j);
                MAP_ADDRESS.add(pos);
            }
        }
    }

    public BoardSurface() {
        mapImg = new Texture(Gdx.files.local("map.png"));
        aSquare = new Array<>();
    }

    public void initialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode aSquareJson = objectMapper.readTree(Gdx.files.local("assets/a_square_json.jsonc").file());
            int count = 0;
            for(JsonNode squareJson : aSquareJson) {
                Vector2 vec = new Vector2(squareJson.get("x").asInt(), squareJson.get("y").asInt());
                int type = squareJson.get("type").asInt();
                if(type == 4) aSquare.add(new TaskSquare(vec, type, count, squareJson.get("document").asText()));
                else if(type == 3) aSquare.add(new EventSquare(vec, type, count));
                else aSquare.add(new Square(vec, type, count));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() { }

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();

        batch.draw(mapImg, 0, 0, MAP_WIDTH, MAP_HEIGHT, 0, 0, MAP_WIDTH, MAP_HEIGHT, false, true);

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
        mapImg.dispose();
    }

    public Square getSquare(int squareNo) {
        return aSquare.get(squareNo);
    }

    public Vector2 getPos(int squareNo) {
        Square s;
        if(squareNo <= aSquare.size-1) s = aSquare.get(squareNo);
        else s = aSquare.peek();
        return s.getAddress();
    }

    public int getSquareCount() {
        return aSquare.size;
    }
}
