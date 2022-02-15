package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class TitleScreen implements Screen {
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

	private Vector3 screenOrigin;
	private Vector3 touchPos;


	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public TitleScreen(final Pawn game) {
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

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, game.LOGICAL_WIDTH, game.LOGICAL_HEIGHT);
		FitViewport uiViewport = new FitViewport(game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT,uiCamera);
		stage = new Stage(uiViewport);

		screenOrigin = new Vector3();
		touchPos = new Vector3();
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);

		//-- 入力
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)  |  Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ENTER)) {
			game.setScreen(new GameScreen(game));
		}

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
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		//-- 論理表示領域を黒で塗りつぶし
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(0,0,0,1);
		renderer.rect(screenOrigin.x, screenOrigin.y,game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT);
		renderer.end();

		//------ 描画


		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.SKY);
		renderer.box(50, 100, 0, 300, 200, 0);
		renderer.end();

		batch.begin();
		font.draw(batch,"タイトル画面",70,120);
		font.draw(batch,"エンターを押して開始！",70,150);
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
	}

}
