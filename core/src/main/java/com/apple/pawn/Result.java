package com.apple.pawn;
//import javax.swing.JFrame;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

//追加
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import javax.swing.JFrame;
import java.util.function.IntSupplier;



public class Result extends UIParts {

    private final BitmapFont font;
    private final Sprite spPiece[];

    public Result(String name, int x, int y, int width, int height,final Pawn game) {
        super(name,x,y,width,height);
        font = game.font;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("piece_atlas.txt"));
        spPiece = new Sprite[6];
        spPiece[0] = atlas.createSprite(Piece.COLOR[0]);
        spPiece[0].flip(false, true);
        spPiece[0].setSize(80,120);
        spPiece[1] = atlas.createSprite(Piece.COLOR[1]);
        spPiece[1].flip(false, true);
        spPiece[1].setSize(80,120);
        spPiece[2] = atlas.createSprite(Piece.COLOR[2]);
        spPiece[2].flip(false, true);
        spPiece[2].setSize(80,120);
        spPiece[3] = atlas.createSprite(Piece.COLOR[3]);
        spPiece[3].flip(false, true);
        spPiece[3].setSize(80,120);
        spPiece[4] = atlas.createSprite(Piece.COLOR[4]);
        spPiece[4].flip(false, true);
        spPiece[4].setSize(80,120);
        spPiece[5] = atlas.createSprite(Piece.COLOR[5]);
        spPiece[5].flip(false, true);
        spPiece[5].setSize(80,120);
    }



    /**
     * initialize　インスタンス作成時以降にしないといけない初期化処理
     * @param playerManager プレイヤー全体を管理。ここから各プレイヤーやそのプレイヤーが持っている駒の情報を取得する
     */
    public void initialize(PlayerManager playerManager,Pawn game) {


//        this.playerManager = playerManager;
//
//        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        param.size = 20;
//        param.incremental = true;			// 自動的に文字を追加
//        param.color = Color.CORAL;			// 文字色
//        param.borderColor = Color.BLACK;	// 境界線色
//        param.borderWidth = 2;				// 境界線の太さ
//        param.flip = true;					// 上下反転
//        fontTitle = game.fontGenerator.generateFont(param);
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
    public void draw(Batch batch, ShapeRenderer renderer, BitmapFont font) {
//        //new TestWindow("テストウィンドウ",400,300);
//        fontTitle.getData().setScale(1, 1);

//
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1.0f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        batch.begin();
        font.getData().setScale(1, 1);
        font.draw(batch, "すごろくゲーム", 100, 500);
//        spPiece[0].setPosition(80, 150);
//        spPiece[1].setPosition(80, 300);
//        spPiece[2].setPosition(80, 450);
//        spPiece[3].setPosition(80, 600);
//        spPiece[0].draw(batch);
//        spPiece[1].draw(batch);
//        spPiece[2].draw(batch);
//        spPiece[3].draw(batch);


        int j=150;
        for(int i=0; i<4; i++){
            spPiece[i].setPosition(80, j);
            spPiece[i].draw(batch);
            j += 140;
        }

        batch.end();
        //StringBuilder txt = new StringBuilder("全員ゴールしたよ");
        //txt.toString();
    }

    /**
     * dispose 読み込んだ画像リソース等を不要になった際に破棄する。(特にリソースを管理しないなら消してもよい)
     */
    public void dispose() {

    }
}



