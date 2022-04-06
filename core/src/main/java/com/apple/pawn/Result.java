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
    private PlayerManager playerManager;
//    private Player player;
    private GameScreen gamescreen;
    private int goalTurn;


    public Result(String name, int x, int y, int width, int height,final Pawn game) {
        super(name,x,y,width,height);
        font = game.font;
        //playerManager = new PlayerManager();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("piece_atlas.txt"));
        spPiece = new Sprite[6];
        for(int i=0; i<6; i++){
            spPiece[i] = atlas.createSprite(Piece.COLOR[i]);
            spPiece[i].flip(false, true);
            spPiece[i].setSize(80,120);
        }
        playerManager = new PlayerManager();
    }



    /**
     * initialize　インスタンス作成時以降にしないといけない初期化処理
     * @param playerManager プレイヤー全体を管理。ここから各プレイヤーやそのプレイヤーが持っている駒の情報を取得する
     */
    public void initialize(PlayerManager playerManager) {


       this.playerManager = playerManager;
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

        //StringBuilder orderBuilder = new StringBuilder();
        //Player player = new Player();

        StringBuilder txt = new StringBuilder("全員ゴールしたよ");
        Iterator<Player> playerIterator = new Array.ArrayIterator<>(playerManager.getGoalPlayer());
        //Player player = playerIterator.next();
        //player = playerIterator.next();
        //txt.append("\n").append(player.getGoalNo()).append("位:").append(player.getName());

//        while(playerIterator.hasNext()) {
//            Player player = playerIterator.next();
//            txt.append("\n").append(player.getGoalNo()).append("位:").append(player.getName());
//        }

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1.0f,0.8f,0.8f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.end();

        batch.begin();
        font.getData().setScale(1, 1);
        //font.draw(batch, "すごろくゲーム", 100, 500);
        //font.draw(batch, player.getName(), 200, 600);
        //font.draw(batch, player.getName(), 200, 130);
        //font.draw(batch, player.getName(), 200, 270);
        //null
        //font.draw(batch,gamescreen.getTurnCount()+"ターン", 320, 130);
        //goalTurn = gamescreen.getTurnCount();
        //font.draw(batch, goalTurn+"ターン", 320, 130);
        font.draw(batch, "ターン数", 320, 130);
        //player.getName()
        //"あいうえお"

        int j=80,turn=130;
        for(int i=0; i<4; i++){
            Player player = playerIterator.next();
            spPiece[i].setPosition(80, j);
            spPiece[i].draw(batch);
            //font.draw(batch, player.getName(), 200, turn);
            font.draw(batch, i+1+"P", 200, turn);
            j += 140;
            turn += 140;
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



