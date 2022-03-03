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
    private GameScreen gameScreen;
    private BoardSurface boardSurface;

    public PlayerManager() {
        aPlayer = new Array<Player>();
    }

    public void initialize(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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
        p.initialize(gameScreen, boardSurface);
        aPlayer.add(p);
        return aPlayer.size;
    }

    public boolean isAllGoal() {
        boolean ret = true;
        for(Player player : aPlayer) {
            if(player.isGoal() == false) ret = false;
        }
        return ret;
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

    public void setBoardSurface(BoardSurface boardSurface) {
        this.boardSurface = boardSurface;
    }

    public Piece[] getPeaces() {
        Array<Piece> p = new Array<Piece>();
        for(Player player : aPlayer) {
            p.add(player.getPiece());
        }
        return p.toArray();
    }

}
