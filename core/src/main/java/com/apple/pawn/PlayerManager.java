package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Objects;

/**
 * プレイヤーを管理するクラス
 * @author fujii
 */
public class PlayerManager {
    public static final int RED = 1;

    private Array<Player> aPlayer;

    //-- 参照
    private GameScreen gameScreen;
    private BoardSurface boardSurface;

    /**
     * コンストラクタ
     */
    public PlayerManager() {
        aPlayer = new Array<>();
    }

    /**
     * 初期化
     * @param gameScreen
     */
    public void initialize(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * ロード時の初期化
     * @param aPlayer
     */
    public void load(Array<Player> aPlayer) {
        this.aPlayer = aPlayer;
        for(Player player : this.aPlayer) player.load(gameScreen, boardSurface, this);
    }

    /**
     * 動作
     */
    public void update() {
        for(Player player : aPlayer) player.update();
    }

    /**
     * 描画
     * @param batch
     */
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

    /**
     * プレイヤー追加
     * @param name プレイヤー名
     * @param pieceColorNo 駒の色ナンバー
     * @return プレイヤー数
     */
    public int add(String name, int pieceColorNo) {
        Player p = new Player(name, pieceColorNo);
        p.initialize(gameScreen, boardSurface, this);
        aPlayer.add(p);
        aPlayer.sort(Comparator.comparingInt(Player::getOrder));
        return aPlayer.size;
    }


    //----------------- getter ----------------------------------------------------------

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
        return aPlayer.get(i);
    }

    public Piece[] getPieces() {
        Array<Piece> p = new Array<>();
        for(Player player : aPlayer) p.add(player.getPiece());
        return p.toArray(Piece.class);
    }

    public Array<Player> getGoalPlayer() {
        Array<Player> goalPlayer = new Array<>();
        for(Player player : aPlayer) {
            if(player.isGoal()) goalPlayer.add(player);
        }

        goalPlayer.sort(Comparator.comparingInt(Player::getGoalNo));
        return goalPlayer;
    }


    //----------------- setter ----------------------------------------------------------

    public void setBoardSurface(BoardSurface boardSurface) {
        this.boardSurface = boardSurface;
    }

}
