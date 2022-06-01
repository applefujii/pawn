package com.apple.pawn;
import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Result extends UIParts {
    public static final int SQUARE_WIDTH = 50, SQUARE_HEIGHT = 50;
    private static final String[] TYPE_STR, TYPE_STR_JP;

    private int space = 0;
    private final UI ui;
    private final Pawn game;
    private final Array<ResultBox> aResultBox;

    static {
        TYPE_STR = new String[Square.TYPE_STR.length - 2];
        TYPE_STR_JP = new String[TYPE_STR.length];
        System.arraycopy(Square.TYPE_STR, 2, TYPE_STR, 0, TYPE_STR.length);
        System.arraycopy(Square.TYPE_STR_JP, 2, TYPE_STR_JP, 0, TYPE_STR_JP.length);
    }

    public Result(String name, int x, int y, int width, int height, int group, PlayerManager playerManager, AssetManager manager,final Pawn game) {
        super(name, x, y, width, height, group);
        px = 20;
        py = 20;
        float spy = (float) (height - (py * 2)) / 14;
        float spx = (float) (width - (px * 2)) / 12;
        float spriteWidth = spy * 2 * ((float) Piece.WIDTH / Piece.HEIGHT);
        Array<Sprite> aSqSprite = new Array<>();
        this.game = game;
        ui = new UI();
        FlagManagement.set(Flag.UI_INPUT_ENABLE);
        FlagManagement.set(Flag.INPUT_ENABLE);
        for (String st : TYPE_STR) {
            Sprite sp = manager.get("map_atlas.txt", TextureAtlas.class).createSprite(st);
            sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
            aSqSprite.add(sp);
        }
        ResultBox.WIDTH = spx;
        ResultBox.HEIGHT = spy;
        aResultBox = new Array<>();
        float x1 = x + px, x2 = x1 + (spx * 2), x3 = x2 + (spx * 4), x4 = x3 + (spx * 2);
        float yY = y + py;
        aResultBox.add(
                new ResultBox(x1, yY, 2, 2),
                new ResultBox(x2, yY, 4, 2, false, true, "名前"),
                new ResultBox(x3, yY, 2, 2, true, true, "ターン数")
        );
        aResultBox.add(
                new ResultBox(x4, yY, 4, 1, true, true, "止まった回数"),
                new ResultBox(x4, yY + spy, 4, 1, true, true, TYPE_STR.length, TYPE_STR_JP)
        );
        for (int i = 0; i < playerManager.getSize(); i++) {
            Player player = playerManager.getGoalPlayer().get(i);
            aResultBox.add(
                    new ResultBox(x1, yY + (spy * 2 * (i + 1)), 2, 2, spriteWidth, spy * 2, player.getPiece().getSprite()),
                    new ResultBox(x2, yY + (spy * 2 * (i + 1)), 4, 2, false, true, player.getName()),
                    new ResultBox(x3, yY + (spy * 2 * (i + 1)), 2, 2, true, true, String.valueOf(player.getGoalTurn())),
                    new ResultBox(x4, yY + (spy * 2 * (i + 1)), 4, 2, SQUARE_WIDTH, SQUARE_HEIGHT, TYPE_STR.length, player.convertToAStringFromAResultDetail(), aSqSprite)
            );
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 0.4f);
        renderer.rect(0, 0, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
        renderer.setColor(1,0.2f,0.2f,1);
        renderer.rect(x, y, width, height);
        renderer.setColor(1, 0.8f, 0.8f, 1);
        renderer.rect(x + px, y + py, width - (px * 2), height - (py * 2));
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        font.getData().setScale(1);

        for (ResultBox resultBox : aResultBox) {
            resultBox.draw(batch, font);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ui.add(new UIPartsSelect("title_link", (x + width) / 2 - 150, (y + height) / 2, 300, 20, 1, 0, true, "タイトルへ戻る","キャンセル"));
        }
    }
}