package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author fujii
 */
public class Player {
    private String name;
    private Piece piece;
    private Dice dice;

    private boolean isGoal;

    public Player(String name, int type) {
        piece = new Piece();
        this.name = name;
        isGoal = false;
    }

    public void initialize(Pawn game) {
        dice = new Dice(game);
    }

    public void update() {
        piece.update();
        dice.update();
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        piece.draw(batch, renderer);
    }

    public void dispose () {
    }

    public Piece getPiece() {
        return piece;
    }

    public String getName() {
        return name;
    }

    public Dice getDice() {
        return dice;
    }

    public void goal() {
        isGoal = true;
        piece.setMove(0);
    }
}
