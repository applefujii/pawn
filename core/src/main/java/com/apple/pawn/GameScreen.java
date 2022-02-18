package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.function.IntSupplier;


public class GameScreen implements Screen {
	public static final int TURN_STANDBY = 10;
	public static final int ACTION_SELECT = 20;
	public static final int DICE_ROLL = 30;
	public static final int PIECE_ADVANCE = 40;
	public static final int TASK_DO = 50;

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

	private Vector3 screenOrigin;			// 画面左上座標
	private Vector3 touchPos;				// タッチ座標
	private int sequenceNo;

	//---- 他のクラス
	private Array<Player> player;			// プレイヤー
	private Player turnPlayer;				// 現在のターンのプレイヤーを指す
	private int turnPlayerNo;				// 何人目のプレイヤーのターンか
	private BoardSurface board;				// 盤面
	private UI ui;							// UI


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
		Gdx.input.setInputProcessor(stage);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, game.LOGICAL_WIDTH, game.LOGICAL_HEIGHT);
		FitViewport uiViewport = new FitViewport(game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT,uiCamera);
		uiStage = new Stage(uiViewport);
		Gdx.input.setInputProcessor(uiStage);

		screenOrigin = new Vector3();
		touchPos = new Vector3();
		turnPlayerNo = -1;

		//-- new
		player = new Array<Player>();
		player.add(new Player("1P", 1));
		player.add(new Player("2P", 2));
		board = new BoardSurface();
		ui = new UI();

		//-- 初期化
		for(Player pl : player) {
			pl.initialize(game);
		}
		ui.initialize(game);
		// フラグ初期化
		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);

		sequenceNo = GameScreen.TURN_STANDBY;
		// 動作させる関数を代入
		sequence = this::turnStandby;
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);

		//------ 入力
		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			game.setScreen(new TitleScreen(game));
		}
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			//-- ワールド座標に変換
			viewport.unproject(touchPos);
		}

		if(FlagManagement.is(Flag.PLAY)) {
			if(FlagManagement.is(Flag.UI_INPUT_ENABLE)) ui.update();
			// シークエンスの動作
			if(FlagManagement.is(Flag.INPUT_ENABLE)) sequence.getAsInt();
		}

		// Flag.PLAY == false
		else {
			//-- Spaceを押すと復帰
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				FlagManagement.set(Flag.PLAY);
			}
		}
	}

	/**
	 * render メインループ、描画。
	 */
	@Override
	public void render (float delta) {
		// 更新
		update();

		// カメラの更新
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		// 塗りつぶし
		ScreenUtils.clear(0, 0, 0, 1);
//		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

		//------ メイン描画
		board.draw(batch, renderer);

		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		if(FlagManagement.is(Flag.UI_VISIBLE)) ui.draw(batch, renderer, font);
		//-- デバッグ表示
		if(FlagManagement.is(Flag.PRINT_DEBUG_INFO)) {
			batch.begin();
			font.getData().setScale(1, 1);
			font.draw(batch, "ScreenOrigin: " + screenOrigin.x + ":" + screenOrigin.y, 0, 0);
			font.draw(batch, "Sequence_no: " + sequenceNo, 0, 16);
			font.draw(batch, "FPS: " +Gdx.graphics.getFramesPerSecond() , 0, 32);
			batch.end();
		}
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
		for(Player pl : player) {
			pl.dispose();
		}
		board.dispose();
	}

	private int turnStandby() {
		if(sequenceNo == TURN_STANDBY) {
			turnPlayerNo++;
			if(turnPlayerNo >= player.size) turnPlayerNo = 0;
			turnPlayer = player.get(turnPlayerNo);
			ui.setDice(turnPlayer.getDice());
			ui.addUiParts(new SelectUIParts("confirm_ready", Pawn.LOGICAL_WIDTH/2-150, 600, turnPlayer.getName()+"の番です"));
			sequenceNo++;
		}
		else {
			//------ 入力
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				sequenceNo = GameScreen.ACTION_SELECT;
				sequence = this::actionSelect;
			}
			//		if(Gdx.input.isKeyPressed(Input.Keys.UP)) camera.zoom-=0.1;
			//		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.zoom+=0.1;
		}

		//---- 動作
		for(Player pl : player) {
			pl.update();
		}
		board.update();
		return 0;
	}

	private int actionSelect() {
		if(sequenceNo == ACTION_SELECT) {
			ui.addUiParts(new SelectUIParts("action_select", Pawn.LOGICAL_WIDTH/2-150, 600, "サイコロを振る", "マップ確認"));
			sequenceNo++;
		}
		else {
			//------ 入力
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				sequenceNo = GameScreen.DICE_ROLL;
				sequence = this::diceRoll;
			}
			//		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-6, 0);
			//		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(6, 0);
			//		if(Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, -6);
			//		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, 6);
		}

		//---- 動作
		for(Player pl : player) {
			pl.update();
		}
		board.update();
		return 0;
	}

	private int diceRoll() {
		Dice dice = turnPlayer.getDice();

		if(sequenceNo == DICE_ROLL) {
			dice.rollStart();
			sequenceNo++;
		}
		else {
			//------ 入力
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				turnPlayer.getPiece().move(dice.rollStop());
				sequenceNo = GameScreen.PIECE_ADVANCE;
				sequence = this::PieceAdvance;
			}
		}

		//---- 動作
		for(Player pl : player) {
			pl.update();
		}
		board.update();
		return 0;
	}

	private int PieceAdvance() {
		if(sequenceNo == PIECE_ADVANCE) {
			ui.addUiParts(new SelectUIParts("move_piece", Pawn.LOGICAL_WIDTH/2-150, 600, "移動"));
			sequenceNo++;
		}
		else {
			//------ 入力
//			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				sequenceNo = GameScreen.TASK_DO;
				sequence = this::taskDo;
//			}
		}

		//---- 動作
		for(Player pl : player) {
			pl.update();
		}
		board.update();
		return 0;
	}

	private int taskDo() {
		if(sequenceNo == TASK_DO) {
			ui.addUiParts(new SelectUIParts("task_result_check", Pawn.LOGICAL_WIDTH/2-150, 600, "成功", "失敗"));
			sequenceNo++;
		}
		else {
			sequenceNo = GameScreen.TURN_STANDBY;
			sequence = this::turnStandby;
		}

		//---- 動作
		for (Player pl : player) {
			pl.update();
		}
		board.update();
		return 0;
	}

}
