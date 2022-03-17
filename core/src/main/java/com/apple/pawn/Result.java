package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Result {
    private boolean drawable; //リザルト描画開始を司るフラグ(Flagで管理したほうがいいかも)
    private PlayerManager playerManager;

    public Result() {
        drawable = false;
    }

    /**
     * initialize　インスタンス作成時以降にしないといけない初期化処理
     * @param playerManager プレイヤー全体を管理。ここから各プレイヤーやそのプレイヤーが持っている駒の情報を取得する
     */
    public void initialize(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * update メインループの描画以外
     */
    public void update() {

    }

    /**
     * draw メインループの描画部分
     * @param batch 画像の表示等を受け持つ。batch.begin()で描画受付開始、batch.end()で描画受付終了
     * @param renderer 直線など簡単な図形の描画を受け持つ。renderer.begin()で描画受付開始、renderer.end()で描画受付終了
     */
    public void draw(Batch batch, ShapeRenderer renderer) {
        if(drawable) {

        }
    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() {

    }

    /**
     * begin フラグを立てる
     */
    public void begin() {
        drawable = true;
    }
}
