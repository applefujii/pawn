package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Iterator;
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
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(this.aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            player.load(gameScreen, boardSurface, this);
        }
    }

    public void update() {
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            player.update();
        }
    }

    public void draw (Batch batch, ShapeRenderer renderer) {
        Player turnPlayer = null;
        batch.begin();
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if(player == gameScreen.getTurnPlayer()) turnPlayer = player;
            else player.draw(batch, renderer);
        }
        if(Objects.nonNull(turnPlayer))turnPlayer.draw(batch, renderer);
        batch.end();
    }

    public void dispose () {
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            player.dispose();
        }
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
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if(!player.isGoal()) ret = false;
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
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            p.add(player.getPiece());
        }
        return p.toArray();
    }

    public Array<Player> getGoalPlayer() {
        Array<Player> goalPlayer = new Array<>();
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(aPlayer);
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if(player.isGoal()) goalPlayer.add(player);
        }

        goalPlayer.sort(Comparator.comparingInt(Player::getGoalNo));
        return goalPlayer;
    }
}
