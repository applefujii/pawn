package com.apple.pawn;

import com.badlogic.gdx.Gdx;
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
    public static int TILE_LENGTH = 256;
    public static int SQUARE_COUNT = 65;
    public static Array<Vector2> MAP_ADDRESS;

    private final Array<Square> aSquare;
    private final TextureAtlas mapAtlas;

    private final Sprite backSprite;

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
        mapAtlas = new TextureAtlas("map_atlas.txt");
        backSprite = mapAtlas.createSprite("back");
        backSprite.flip(false, true);
        aSquare = new Array<>();

        initialize();
    }

    private void initialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode aSquareJson = objectMapper.readTree(Gdx.files.local("assets/a_square_json.jsonc").file());
            int count = 0;
            for(JsonNode squareJson : aSquareJson) {
                Vector2 vec = new Vector2(squareJson.get("x").asInt(), squareJson.get("y").asInt());
                if(squareJson.get("type").asInt() == 0 || squareJson.get("type").asInt() == 2) {
                    aSquare.add(new Square(
                            vec,
                            squareJson.get("type").asInt(),
                            count,
                            squareJson.get("document").asText(),
                            mapAtlas
                    ));
                } else if(squareJson.get("has_task").asBoolean()) {
                    aSquare.add(new TaskSquare(
                            vec,
                            squareJson.get("type").asInt(),
                            count,
                            squareJson.get("document").asText(),
                            mapAtlas
                    ));
                } else {
                    aSquare.add(new EventSquare(
                            vec,
                            squareJson.get("type").asInt(),
                            count,
                            squareJson.get("document").asText(),
                            mapAtlas
                    ));
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {}

    public void draw (Batch batch, ShapeRenderer renderer) {
        batch.begin();

        backSprite.setSize(TILE_LENGTH, TILE_LENGTH);
        Iterator<Vector2> mapAddressIterator = new Array.ArrayIterator<>(MAP_ADDRESS);
        while(mapAddressIterator.hasNext()) {
            Vector2 vec = mapAddressIterator.next();
            backSprite.setPosition(TILE_LENGTH*vec.x, TILE_LENGTH*vec.y);
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

    public Square getSquare(int squareNo) { return aSquare.get(squareNo); }

    public Vector2 getPos(int squareNo) {
        Square s;
        if(squareNo <= aSquare.size-1) s = aSquare.get(squareNo);
        else s = aSquare.peek();
        return s.getAddress();
    }

    public int getSquareCount() { return aSquare.size; }
}
