package com.apple.pawn;

import android.support.annotation.NonNull;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;

public class Result extends UIParts {
    private static final int INDEX_WIDTH = 70,INDEX_HEIGHT = 130;
    public static final String[] TYPE_STR = {"normal", "event", "task"};
    public static final int SQUARE_WIDTH = 50, SQUARE_HEIGHT = 50;
    private static final Array<String> TYPE_STR_JP;

    private final PlayerManager playerManager;
    private final float spx;
    private final float span;
    private final float spriteWidth;
    private final Array<Sprite> aSqSprite;

    static {
        //TYPE_STR = new Array<>();
        //TYPE_STR.addAll(Square.TYPE_STR, 2, Square.TYPE_STR.length - 2);
        TYPE_STR_JP = new Array<>();
        TYPE_STR_JP.addAll(Square.TYPE_STR_JP, 2, Square.TYPE_STR_JP.length - 2);
    }

    public Result(String name, int x, int y, int width, int height, int group, PlayerManager playerManager, AssetManager manager) {
        super(name,x,y,width,height,group);
        //Gdx.app.debug("fps", "height="+height);
        this.playerManager = playerManager;
        spx = (float) (width - INDEX_WIDTH - px) / 6;
        span = (float) (height - INDEX_HEIGHT - py) / 6;
        spriteWidth = span * ((float) Piece.WIDTH / Piece.HEIGHT);
        aSqSprite = new Array<>();
        for(String st : TYPE_STR) {
            Sprite sp = manager.get("assets/map_atlas.txt", TextureAtlas.class).createSprite(st);
            sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
            aSqSprite.add(sp);
        }
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
    public void draw(@NonNull SpriteBatch batch, @NonNull ShapeRenderer renderer, @NonNull BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1.0f,0.8f,0.8f,1);
        renderer.rect(x, y, width, height);
        renderer.end();

        batch.begin();
        font.getData().setScale(1);
        PawnUtils.fontDrawXCenter(font, batch, "名前", 280, 60);
        PawnUtils.fontDrawXCenter(font, batch, "ターン数", 500, 60); //35
        PawnUtils.fontDrawXCenter(font, batch, "止まった回数", 900, 60);
        int w = 700;
        for(String st : TYPE_STR_JP) {
            PawnUtils.fontDrawXCenter(font, batch, st, w, 100);
            w += 200;
        }

        float g = INDEX_WIDTH + (spx / 2);
        float h = INDEX_HEIGHT + (span / 2);
        for(Player player : playerManager.getGoalPlayer()){
            Sprite sprite = player.getPiece().getSprite();
            Gdx.app.debug("fps", "span="+span);
            sprite.setSize(spriteWidth, span);
            sprite.setCenter(g, h);
            sprite.draw(batch);
            PawnUtils.fontDrawYCenter(font, batch, player.getName(), 260, h);
            PawnUtils.fontDrawYCenter(font, batch, player.getGoalTurn()+"ターン", 465, h);
            w = 700;
            for(int i = 0; i < TYPE_STR.length; i++) {
                Sprite sqSprite = aSqSprite.get(i);
                sqSprite.setCenter(w, h);
                sqSprite.draw(batch);
                PawnUtils.fontDrawCenter(font, batch, String.valueOf(player.getAResultDetail().get(i)), w, h);
                w += 200;
            }
            h += span;
        }

        batch.end();
    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() { }
}



