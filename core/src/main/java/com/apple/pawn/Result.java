package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Result extends UIParts {
    private static final int INDEX_HEIGHT = 110;

    private final PlayerManager playerManager;
    private final float span;
    private final float spriteWidth;

    public Result(String name, int x, int y, int width, int height, int group, PlayerManager playerManager) {
        super(name,x,y,width,height,group);
        this.playerManager = playerManager;
        span = (float) (height - INDEX_HEIGHT) / 6;
        spriteWidth = span * ((float) Piece.WIDTH / Piece.HEIGHT);
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
        font.getData().setScale(1);
        font.draw(batch, "名前", 200, 60);
        font.draw(batch, "ターン数", 320, 60);
        font.draw(batch, "どのマスに何回止まったか", 440, 60);

        float k = INDEX_HEIGHT;
        for(Player player : playerManager.getGoalPlayer()){
            Sprite sprite = player.getPiece().getSprite();
            sprite.setSize(spriteWidth, span);
            sprite.setCenter(80 + (spriteWidth / 2), k + (span / 2));
            sprite.draw(batch);
            font.draw(batch, player.getName(), 200, k);
            font.draw(batch, player.getGoalTurn()+"ターン", 320, k);
            font.draw(batch, "aRD="+player.getAResultDetail(), 440, k);
            k += span;
        }

        batch.end();
    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() { }
}



