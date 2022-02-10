package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class GameScreen implements Screen {
	private final Pawn game;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer renderer;

	private OrthographicCamera camera;		// カメラ
	private OrthographicCamera uiCamera;	// UIカメラ
	private FitViewport viewport;
	private FitViewport uiViewport;
	private Stage stage;					// カメラとビューポートの管理
	private Stage uiStage;					// UIのカメラとビューポートの管理
	private Group topGroup;

	private Vector3 screenOrigin;			// 画面左上座標
	private Vector3 touchPos;				// タッチ座標

	private Player player;					// プレイヤー

	private FlagManagement fm;


	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public GameScreen (final Pawn game) {
		this.game = game;
		batch = game.batch;
		font = game.font;
		renderer = game.renderer;

		camera = new OrthographicCamera();
		camera.setToOrtho(true, game.LOGICAL_WIDTH, game.LOGICAL_HEIGHT);
		viewport = new FitViewport(game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT,camera);
		uiViewport = new FitViewport(game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT,camera);
		stage = new Stage(viewport);
		uiStage = new Stage(uiViewport);
		topGroup = new Group();
		stage.setRoot(topGroup);		// 最上位グループを指定
		Gdx.input.setInputProcessor(stage);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, game.LOGICAL_WIDTH, game.LOGICAL_HEIGHT);
		FitViewport uiViewport = new FitViewport(game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT,uiCamera);
		uiStage = new Stage(uiViewport);
		Gdx.input.setInputProcessor(uiStage);

		screenOrigin = new Vector3();
		touchPos = new Vector3();

		player = new Player();
		topGroup.addActor(player);

		fm = new FlagManagement();
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);

		//------ 入力
		if(Gdx.input.isKeyPressed(Input.Keys.F1)) { game.setScreen(new TitleScreen(game)); }
		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			//-- ワールド座標に変換
			viewport.unproject(touchPos);
		}
		if(Gdx.input.justTouched()) {
			//-- 衝突判定
			Rectangle rect = new Rectangle();
			rect.set(touchPos.x,touchPos.y,1,1);
			Rectangle touchRect = new Rectangle();
			boolean isCol = Intersector.intersectRectangles(player.getRect(), rect, touchRect);
			if(isCol) Gdx.app.debug("info", "タッチ: x."+touchRect.x+" y."+touchRect.y+" w."+touchRect.width+" h."+touchRect.height);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-6, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(6, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, -6);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, 6);

		//---- 動作
		player.move(touchPos);
	}

	/**
	 * render メインループ、描画。
	 */
	@Override
	public void render (float delta) {
		update();

		// カメラの更新
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		// 塗りつぶし
		ScreenUtils.clear(0, 0, 0, 1);
		//-- 論理表示領域を黒で塗りつぶし
//		renderer.begin(ShapeRenderer.ShapeType.Filled);
//		renderer.setColor(0,0,0,1);
//		renderer.rect(screenOrigin.x, screenOrigin.y,game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT);
//		renderer.end();

		//------ 描画
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		for( int i=0 ; i<20 ; i++ ) {
			renderer.box(i*100, 0, 0, 1, 2000, 0);
			renderer.box(0, i*100, 0, 2000, 1, 0);
		}
		renderer.end();

		stage.draw();

		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		font.draw(batch,"ScreenOrigin: "+screenOrigin.x+":"+screenOrigin.y,0,0);
		batch.end();
	}

	/**
	 * resize リサイズイベント
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		// ビューポートの更新
		viewport.update(width, height);
		uiViewport.update(width, height);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() { dispose(); }

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	/**
	 * dispose 解放
	 */
	@Override
	public void dispose () {
		player.dispose();
	}

}
