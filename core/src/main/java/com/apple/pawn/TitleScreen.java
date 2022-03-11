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

import java.util.function.IntSupplier;


public class TitleScreen implements Screen {

	private final Pawn game;
	// 動作させるシークエンス
	private IntSupplier sequence;
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
	private int sequenceNo;					// シークエンス番号

	private UI ui;							// UI

	private GameSetting gameSetting;

	//-- 参照
	private UIPartsSelect selectUI;


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

		ui = new UI();
		ui.initialize(game);

		gameSetting = new GameSetting();

		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);

		sequenceNo = 1;
		sequence = this::homeSequence;
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

		if(FlagManagement.is(Flag.INPUT_ENABLE)) sequence.getAsInt();

		ui.update();
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
		font.getData().setScale(1, 1);
		batch.begin();
		font.draw(batch, "ScreenOrigin: x:" + screenOrigin.x + " y:" + screenOrigin.y, 0, 16*0);
		font.draw(batch, "CameraPosition: x:" + camera.position.x + " y:" + camera.position.y+ " zoom:" + camera.zoom, 0, 16*1);
		font.draw(batch, "Sequence_no: " + sequenceNo, 0, 16*2);
		font.draw(batch, "FPS: " +Gdx.graphics.getFramesPerSecond() , 0, 16*3);
		batch.end();

		ui.draw(batch, renderer, font);
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

	private int homeSequence() {
		if(sequenceNo == 1) {
			ui.add(new UIPartsSelect("title_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, true, "開始", "続きから", "設定"));
			sequenceNo++;
		}
		if(sequenceNo == 2) {
			int select = ui.getSelect();
			// 開始
			if (select == 0) {
				sequence = this::startSettingSequence;
				sequenceNo = 1;
			}
			// 続きから
			if (select == 1) {
				sequenceNo = 1;
			}
			// 設定
			if (select == 2) {
				sequenceNo = 1;
			}
		}
		return 0;
	}

	private int startSettingSequence() {
		if(sequenceNo == 1) {
			ui.add(new UIPartsSelectIndex("setting_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, true, "プレイ人数", "2人", "3人", "4人", "5人", "6人"));
			selectUI = (UIPartsSelect)ui.getUIParts("setting_menu");
			sequenceNo++;
		}
		// 人数
		if(sequenceNo == 2) {
			int select = ui.getSelect();
			gameSetting.init(select+2);
			sequenceNo = 3;
		}
		if(sequenceNo == 3) {
		}
		return 0;
	}

}
