package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Objects;

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
        aPlayer = new Array<>();
    }

    public void initialize(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        //Gdx.app.debug("fps", "gameScreen="+this.gameScreen);
    }

    public void load(Array<Player> aPlayer) {
        this.aPlayer = aPlayer;
        for(Player player : this.aPlayer) player.load(gameScreen, boardSurface, this);
    }

    public void update() {
        for(Player player : aPlayer) player.update();
    }

    public void draw (@NonNull Batch batch) {
        Player turnPlayer = null;
        batch.begin();
        for(Player player : aPlayer) {
            if(player == gameScreen.getTurnPlayer()) turnPlayer = player;
            else player.draw(batch);
        }
        if(Objects.nonNull(turnPlayer))turnPlayer.draw(batch);
        batch.end();
    }

    public void dispose () {
        for(Player player : aPlayer) player.dispose();
    }

    public int add(String name, int pieceColorNo) {
        Player p = new Player(name, pieceColorNo);
        p.initialize(gameScreen, boardSurface, this);
        aPlayer.add(p);
        aPlayer.sort(Comparator.comparingInt(Player::getOrder));
        return aPlayer.size;
    }

    public boolean isAllGoal() {
        boolean ret = true;
        for(Player player : aPlayer) {
            if(!player.isGoal()) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public int getSize() {
        return aPlayer.size;
    }

    public Array<Player> getAPlayer() {
        return aPlayer;
    }

    public Player getPlayer(int i) {
        //Gdx.app.debug("fps", "turnPlayer="+aPlayer.get(i).toString());
        return aPlayer.get(i);
    }

    public void setBoardSurface(BoardSurface boardSurface) {
        this.boardSurface = boardSurface;
    }

    public Piece[] getPieces() {
        Array<Piece> p = new Array<>();
        for(Player player : aPlayer) p.add(player.getPiece());
        return p.toArray();
    }

    public Array<Player> getGoalPlayer() {
        Array<Player> goalPlayer = new Array<>();
        for(Player player : aPlayer) {
            if(player.isGoal()) goalPlayer.add(player);
        }

        goalPlayer.sort(Comparator.comparingInt(Player::getGoalNo));
        return goalPlayer;
    }
}
