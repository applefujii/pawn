package com.apple.pawn;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Timer;
import java.util.TimerTask;

public class Pawn extends Game {
	public static final int LOGICAL_WIDTH = 1280;
	public static final int LOGICAL_HEIGHT = 720;

	public SpriteBatch batch;
	public FreeTypeFontGenerator fontGenerator;
	public BitmapFont font;
	public ShapeRenderer renderer;
	public RandomXS128 random;
	public AssetManager manager;

	public OrthographicCamera uiCamera;	// UIカメラ
	public FitViewport uiViewport;
	public Stage uiStage;					// UIのカメラとビューポートの管理
	public Achievement achievement;
	private float timer;
	private long totalFrame = 0;
	private int frame = 0;
	private Timer fpsTimer;
	private Screen nextScreen;

	/**
	 * create 初期化、読み込み
	 */
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("font/mplus-1m-medium.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 16;
		param.incremental = true;			// 自動的に文字を追加
		param.color = Color.WHITE;			// 文字色
		param.borderColor = Color.BLACK;	// 境界線色
		param.borderWidth = 2;				// 境界線の太さ
		param.flip = true;					// 上下反転
		font = fontGenerator.generateFont(param);
		random = new RandomXS128(System.currentTimeMillis());
		manager = new AssetManager();
		manager.load("assets/piece_atlas.txt", TextureAtlas.class);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, LOGICAL_WIDTH, LOGICAL_HEIGHT);
		uiViewport = new FitViewport(LOGICAL_WIDTH,LOGICAL_HEIGHT,uiCamera);
		uiStage = new Stage(uiViewport);
		achievement = new Achievement(timer);

		//-- FPSをデバッグ出力
		TimerTask fpsTask = new TimerTask() {
			public void run() {
//				Gdx.app.debug("fps", "fps="+frame);
				frame = 0;
			}
		};
		timer = 0;
		fpsTimer = new Timer();
		fpsTimer.scheduleAtFixedRate(fpsTask, 0, 1000);

		GameSetting setting = new GameSetting();
		setting.init(4);
		setting.setStageNo(0);
		GameScreen gameScreen = new GameScreen(this);
		gameScreen.initialize(setting);
		this.setScreen(gameScreen);
	}

	/**
	 * render メインループ
	 */
	@Override
	public void render () {
		timer += Gdx.graphics.getDeltaTime();
		totalFrame++;
		frame++;
		// F4で終了
//		if(Gdx.input.isKeyPressed(Input.Keys.F4)) Gdx.app.exit();

		super.render();

		if(nextScreen != null) {
			super.setScreen(nextScreen);
			nextScreen = null;
		}
	}

	@Override
	public void setScreen(Screen screen) {
		nextScreen = screen;
	}

	@Override
	public void resize(int width, int height) {
		// ビューポートの更新
		uiViewport.update(width, height);
	}

	/**
	 * dispose 解放
	 */
	@Override
	public void dispose () {
		batch.dispose();
		fontGenerator.dispose();
		font.dispose();
		renderer.dispose();
		fpsTimer.cancel();
		manager.dispose();
		achievement.dispose();
	}

	public float getTimer() {
		return timer;
	}

}
