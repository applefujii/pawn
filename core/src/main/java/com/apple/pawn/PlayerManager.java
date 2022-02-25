package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * @author fujii
 */
public class PlayerManager {
    public static final int RED = 1;

    private Array<Player> aPlayer;

    //-- 参照
    private Pawn game;

    public PlayerManager() {
        aPlayer = new Array<Player>();
    }

    public void initialize(Pawn game) {
        this.game = game;
    }

    public void update() {
        for(Player player : aPlayer) {
            player.update();
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        for(Player player : aPlayer) {
            player.draw(batch, renderer);
        }
    }

    public void dispose () {
        for(Player player : aPlayer) {
            player.dispose();
        }
    }

    public int add(String name, int pieceColorNo) {
        Player p = new Player(name, pieceColorNo);
        aPlayer.add(p);
        return aPlayer.size;
    }

    public int getSize() {
        return aPlayer.size;
    }

    public Array<Player> getPlayer() {
        return aPlayer;
    }

    public Player getPlayer(int i) {
        return aPlayer.get(i);
    }
}
