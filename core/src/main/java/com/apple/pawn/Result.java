package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Result extends UIParts {
    private final PlayerManager playerManager;
    //private final GameSetting gamesetting;

    public Result(String name, int x, int y, int width, int height, int group, PlayerManager playerManager) {
        super(name,x,y,width,height,group);
        this.playerManager = playerManager;
        //Gdx.app.debug("fps", "width="+width);
        //Gdx.app.debug("fps", "height="+height);
    }


    /**
     * update メインループの描画以外
     */
    public int update() {
        return 0;
    }

    /**
     * draw メインループの描画部分
     * @param batch 画像の表示等を受け持つ。batch.begin()で描画受付開始、batch.end()で描画受付終了
     * @param renderer 直線など簡単な図形の描画を受け持つ。renderer.begin()で描画受付開始、renderer.end()で描画受付終了
     */
    public void draw(@NonNull Batch batch, @NonNull ShapeRenderer renderer, @NonNull BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1.0f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        batch.begin();
        font.getData().setScale(1, 1);
        font.draw(batch, "名前", 200, 60);
        font.draw(batch, "ターン数", 320, 60);
        font.draw(batch, "どのマスに何回止まったか", 440, 60);
        //Gdx.app.debug("fps", "getGoalPlayer="+playerManager.getGoalPlayer());

        int k = 110;
        for(Player player : playerManager.getGoalPlayer()){
            Sprite sprite = player.getPiece().getSprite();
            //sprite.setScale((float)0.8, (float)0.8);

            sprite.setScale(4/playerManager.getGoalPlayer().size);
            //sprite.setScale((float)0.8);
            //sprite.setSize(60, 90);
            sprite.setPosition(80,k-20);

            //sprite.setPosition(80,k-50);
            sprite.draw(batch);
            font.draw(batch, player.getName(), 200, k);
            font.draw(batch, player.getGoalTurn()+"ターン", 320, k);
            font.draw(batch, "aRD="+player.getAResultDetail(), 440, k);
            k += height/playerManager.getGoalPlayer().size-10;
            //Gdx.app.debug("fps", "getGoalPlayer="+playerManager.getGoalPlayer());
            //Gdx.app.debug("fps", "player="+player);
        }

        batch.end();
    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() { }
}



