package com.apple.pawn;
import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Result extends UIParts {
    public static final int INDEX_WIDTH = 120;
    public static final int SQUARE_WIDTH = 50, SQUARE_HEIGHT = 50;
    private static final Array<String> TYPE_STR, TYPE_STR_JP;

    private final PlayerManager playerManager;
    private final float span;
    private final float spx;
    private final float spy;
    private final float spriteWidth;
    private final float[] multx = {2,4,(float)6.2};
    private final float rlen;
    private final float rx;
    private final float inc;
    private int space = 0;
    private final String[] item = {"名前","ターン数","止まった回数"};
    private final UI ui;
    private final Array<Sprite> aSqSprite;
    private final Pawn game;

    static {
        TYPE_STR = new Array<>();
        TYPE_STR.addAll(Square.TYPE_STR, 2, Square.TYPE_STR.length - 2);
        TYPE_STR_JP = new Array<>();
        TYPE_STR_JP.addAll(Square.TYPE_STR_JP, 2, Square.TYPE_STR_JP.length - 2);
    }

    public Result(String name, int x, int y, int width, int height, int group, PlayerManager playerManager, AssetManager manager,final Pawn game) {
        super(name, x, y, width, height, group);
        this.playerManager = playerManager;
        px = 130;
        py = 160;
        span = (float) (height - py) / 6;
        spx = (float) (width - px) / 7;
        spy = height / 7 - height % 7;
        rx = (float) (spx * 4.5);
        rlen = width - rx;
        inc = (rlen - (rlen % 3)) / 3;
        spriteWidth = span * ((float) Piece.WIDTH / Piece.HEIGHT);
        aSqSprite = new Array<>();
        this.game = game;
        ui = new UI();
        FlagManagement.set(Flag.UI_INPUT_ENABLE);
        FlagManagement.set(Flag.INPUT_ENABLE);
        for (String st : TYPE_STR) {
            Sprite sp = manager.get("assets/map_atlas.txt", TextureAtlas.class).createSprite(st);
            sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
            aSqSprite.add(sp);
        }
        select();
    }

    /**
     * update メインループの描画以外
     */
    public int update() {
        int select = ui.getSelect();
        if(FlagManagement.is(Flag.UI_INPUT_ENABLE) && space > 1) ui.update();

        if(select != -1 ) {
            if (select == 0) {
                game.setScreen(new TitleScreen(game));
            }
            if (select == 1) {
                space = 1;
            }
        }
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

        for(int i=0; i<multx.length; i++){
            PawnUtils.fontDrawXCenter(font, batch, item[i], spx * multx[i], spy);
        }

        float w = inc/2;
        float h = spy*2;
        for(String st : TYPE_STR_JP) {
            PawnUtils.fontDrawXCenter(font, batch, st, rx+w, h-SQUARE_HEIGHT);
            w += inc;
        }
        float g = INDEX_WIDTH + (spriteWidth / 2);
        for(Player player : playerManager.getGoalPlayer()){
            Sprite sprite = player.getPiece().getSprite();
            sprite.setSize(spriteWidth, span);
            sprite.setCenter(g, h);
            sprite.draw(batch);
            PawnUtils.fontDrawCenter(font, batch, player.getName(), spx * multx[0], h);
            PawnUtils.fontDrawCenter(font, batch, player.getGoalTurn()+"ターン", spx * multx[1], h);
            w = inc/2;
            for(int i = 0; i < TYPE_STR.size; i++) {
                Sprite sqSprite = aSqSprite.get(i);
                sqSprite.setCenter(rx+w, h);
                sqSprite.draw(batch);
                PawnUtils.fontDrawCenter(font, batch, String.valueOf(player.getAResultDetail().get(i)), rx+w, h);
                w += inc;
            }
            h += spy;
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            space += 1;
        }
        if(space > 1){
            ui.draw(batch, renderer, font, 1);
            select();
        }



    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() { }

    public void select(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ui.add(new UIPartsSelect("title_link", (x + width) / 2 - 150, (y + height) / 2, 300, 20, 1, 0, true, "タイトルへ戻る","キャンセル"));
        }
    }
}