package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


public class Result extends UIParts {

    private final BitmapFont font;
    Piece[] spPiece;
    private PlayerManager playerManager;
//    private Player player;
    private GameScreen gamescreen;
    private GameSetting gameSetting;
    private int goalTurn;
    int[] aSquareNo;
    //int playerNo;
    Player player;

    public Result(String name, int x, int y, int width, int height, int group,final Pawn game,GameSetting gameSetting) {
        super(name,x,y,width,height,group);
        font = game.font;
        this.aSquareNo = new int[6];
        this.gameSetting = gameSetting;
        //this.playerNo = playerNo;
        //Gdx.app.debug("fps", "aResultDetail="+aResultDetail);
        //this.aSquareNo[0] = 0;
        //Gdx.app.debug("fps", "aSquareNo[0]="+aSquareNo[0]);
        //playerManager = new PlayerManager();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("piece_atlas.txt"));
//        spPiece = new Sprite[6];
//        for(int i=0; i<6; i++){
//            spPiece[i] = atlas.createSprite(Piece.COLOR[i]);
//            spPiece[i].flip(false, true);
//            //spPiece[i].setSize(80,120);
//            spPiece[i].setSize(60,90);
//        }
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
        //new TestWindow("テストウィンドウ",400,300);
//        fontTitle.getData().setScale(1, 1);

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
        //renderer.setColor(0,0,255,0);
        //renderer.box(50,50,0,120,100,0);
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
        //font.draw(batch, aSquareNo[0]+"ターン", 320, 130);
        //font.draw(batch, "ターン数", 320, 130);
        //player.getName()
        //"あいうえお"
        font.draw(batch, "名前", 200, 60);
        font.draw(batch, "ターン数", 320, 60);
        font.draw(batch, "どのマスに何回止まったか", 440, 60);
        int j=70,k=110;
        for(int i=0; i<gameSetting.getPlayerNo(); i++){
            Player player = playerIterator.next();
            spPiece = playerManager.getPieces();
            //font.draw(batch, player.getName(), 200, turn);
            font.draw(batch, i+1+"P", 200, k);
            font.draw(batch, player.getGoalTurn()+"ターン", 320, k);

            font.draw(batch, "aRD="+player.getAResultDetail(), 440, k);
            //font.draw(batch, aResultDetail+"sample", 440, k);

//            Iterator<Integer> iterator = aResultDetail.iterator();
//            int ab = 100;
//            int cd = 440;
//            while(iterator.hasNext()) {
//                int num = iterator.next();
//                //System.out.println(num);
//                font.draw(batch, num+"sample", cd, k);
//                cd += ab;
//                Gdx.app.debug("fps", "RDetail="+num);
//            }

            j += 100;
            k += 100;
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



