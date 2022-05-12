package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * プレイヤークラス
 * @author fujii
 */
public class Player {
    @JsonProperty
    private String name;                // 名前
    @JsonProperty
    private Piece piece;                // 駒
    @JsonProperty
    private Array<Integer> aDiceNo;     // 過去に出したサイコロの目
    @JsonProperty
    private boolean isGoal;             // ゴールしているか
    @JsonProperty
    private int goalNo;
    @JsonProperty
    private int goalTurn;
    @JsonProperty
    private Array<Integer> aResultDetail;
    @JsonProperty
    private int order;

    //-- 参照
    @JsonIgnore
    private GameScreen gameScreen;
    @JsonIgnore
    private BoardSurface boardSurface;
    @JsonIgnore
    private PlayerManager playerManager;

    // jacksonで読み込むときにデフォルトコンストラクタが必要
    public Player() {
    }

    /**
     * コンストラクタ
     * @param name プレイヤー名
     * @param pieceColorNo 駒の色ナンバー
     */
    public Player(String name, int pieceColorNo) {
        this.name = name;
        piece = new Piece(pieceColorNo);
        aDiceNo = new Array<>();
        isGoal = false;
        goalNo = 0;
        goalTurn = 0;
        aResultDetail = new Array<>();
        aResultDetail.add(0, 0, 0);
        order = MathUtils.random(0, 255);
    }

    /**
     * 初期化
     * @param gameScreen
     * @param bs 盤面
     * @param playerManager アセットの読み込みに使用
     */
    public void initialize(GameScreen gameScreen, BoardSurface bs, PlayerManager playerManager) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        this.playerManager = playerManager;
        piece.initialize(bs, this.gameScreen.getManager());
    }

    /**
     * ロード時の初期化
     * @param gameScreen
     * @param bs 盤面
     * @param playerManager アセットの読み込みに使用
     */
    public void load(GameScreen gameScreen, BoardSurface bs, PlayerManager playerManager) {
        this.gameScreen = gameScreen;
        this.boardSurface = bs;
        this.playerManager = playerManager;
        piece.load(bs, this.gameScreen.getManager());
    }

    /**
     * 動作
     */
    public void update() {
        if(piece.update()) {
            gameScreen.addGoalNo();
            goalNo = gameScreen.getGoalNo();
            goalTurn = gameScreen.getTurnCount();
            isGoal = true;
        }
    }

    /**
     * 描画
     * @param batch
     */
    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        piece.draw(batch,renderer,font,name);
    }

    public void dispose () {
        piece.dispose();
    }

    public void addResultDetail(@NonNull Square visitSquare) {
        int type = visitSquare.getType() - 2;
        if(type >= 0) {
            int count = aResultDetail.get(type);
            Gdx.app.debug("aResultDetail", aResultDetail.toString());
            Gdx.app.debug("type", Square.TYPE_STR_JP[type + 2]);
            Gdx.app.debug("count(before)", String.valueOf(count));
            count++;
            Gdx.app.debug("count(after)", String.valueOf(count));
            aResultDetail.set(type, count);
        }
    }

    public void removeResultDetail(@NonNull Square visitSquare) {
        int type = visitSquare.getType() - 2;
        if(type >= 0) {
            int count = aResultDetail.get(type);
            count--;
            aResultDetail.set(type, count);
        }
    }

    /**
     * サイコロの目の履歴に追加
     * @param diceNo サイコロの目
     */
    public void addADiceNo(int diceNo) {
        if(aDiceNo.size > 10-1) aDiceNo.pop();
        aDiceNo.insert(0, diceNo);
    }


    //----------------- getter ----------------------------------------------------------

    public boolean isGoal() {
        return isGoal;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGoalNo() { return goalNo; }

    public int getGoalTurn() {
        return goalTurn;
    }

    public int getOrder() {
        return order;
    }

    public Array<Integer> getAResultDetail() {
        return aResultDetail;
    }

    public Array<Integer> getADiceNo() {
        return aDiceNo;
    }
}
