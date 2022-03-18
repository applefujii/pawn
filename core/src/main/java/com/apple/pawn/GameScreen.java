package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Iterator;
import java.util.function.IntSupplier;

/**
 * @author fujii
 */
public class GameScreen implements Screen {

	private final Pawn game;
	// 動作させるシークエンス
	private IntSupplier sequence;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final ShapeRenderer renderer;

	private final OrthographicCamera camera;		// カメラ
	private final OrthographicCamera uiCamera;	// UIカメラ
	private final FitViewport viewport;
	private final FitViewport uiViewport;
	private final Stage stage;					// カメラとビューポートの管理
	private Stage uiStage;					// UIのカメラとビューポートの管理

	private float timer;
	private float timerRap;
	private final Vector3 screenOrigin;			// 画面左上座標
	private final Vector3 touchPos;				// タッチ座標
	private int sequenceNo;					// シークエンス番号
	private int turnPlayerNo;				// 何人目のプレイヤーのターンか
	private int goalNo;						// ゴールした人数
	private float zoom;						// ズーム率
	private int move;
	private int back;
	private final float mapCameraZoom;
	private final float mapCameraHeight;
	private final float mapCameraWidth;
	private int turnCount;

	//---- 他のクラス
	private GameSetting gameSetting;				// ゲームの設定
	private final PlayerManager playerManager;		// プレイヤー管理
	private final BoardSurface board;				// 盤面
	private final Dice dice;						// さいころ
	private final UI ui;							// UI
	private final FileIO fileIO;					// セーブファイルの読み書き
	private final SaveData saveData;				// セーブファイル

	//---- 参照
	private Player turnPlayer;				// 現在のターンのプレイヤーを指す


	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public GameScreen (final Pawn game) {
		this.game = game;
		batch = game.batch;
		font = game.font;
		renderer = game.renderer;
		timer = 0;
		goalNo = 0;
		sequenceNo = 1;
		turnPlayerNo = 1;
		zoom = 1.0f;
		mapCameraZoom = (float) BoardSurface.MAP_HEIGHT / Pawn.LOGICAL_HEIGHT;
		mapCameraHeight = (float) BoardSurface.MAP_HEIGHT / 2;
		mapCameraWidth = (float) BoardSurface.MAP_WIDTH / 2;
		turnCount = 1;

		//---- カメラ関係の初期化
		camera = new OrthographicCamera();
		camera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		viewport = new FitViewport(Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT,camera);
		uiViewport = new FitViewport(Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT,camera);
		stage = new Stage(viewport);
		uiStage = new Stage(uiViewport);
		Gdx.input.setInputProcessor(stage);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		FitViewport uiViewport = new FitViewport(Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT,uiCamera);
		uiStage = new Stage(uiViewport);
		Gdx.input.setInputProcessor(uiStage);

		//---- その他の初期化
		//-- new
		screenOrigin = new Vector3();
		touchPos = new Vector3();
		playerManager = new PlayerManager();
		board = new BoardSurface();
		dice = new Dice(game);
		ui = new UI();
		fileIO = new FileIO();
		saveData = new SaveData();
		//-- 初期化
		board.initialize();
		playerManager.initialize(this);
		turnPlayerNo = -1;
		ui.initialize(game);
		//-- 参照セット
		playerManager.setBoardSurface(board);
		ui.setDice(dice);
		fileIO.setSaveData(saveData);
		saveData.aPlayer = playerManager.getAPlayer();
		//-- 作成
		ui.add(new UIPartsExplanation(UI.SQUARE_EXPLANATION, Pawn.LOGICAL_WIDTH-310, 100, 300, 360, "マスの説明。折り返しできるようにしないとはみ出る。改行するとバグるので修正が必要。\n(追記)改行文字で改行可能に。"));
		// フラグ初期化
		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);
		FlagManagement.set(Flag.LOOK_PIECE);

		sequenceNo = Sequence.TURN_STANDBY.no;
		// 動作させる関数を代入
		sequence = this::turnStandby;
	}

	public void initialize(final GameSetting setting) {
		this.gameSetting = setting;
		String name[] = gameSetting.getAName();
		int color[] = gameSetting.getAColorNo();
		for(int i=0 ; i<name.length ; i++) {
			playerManager.add(name[i], color[i]);
		}
	}

	public void load(final SaveData sd) {
		playerManager.load(sd.aPlayer);
		timer = sd.timer;
		goalNo = sd.goalNo;
//		sequenceNo = sd.sequenceNo;
		turnPlayerNo = sd.turnPlayerNo-1;
		saveData.aPlayer = playerManager.getAPlayer();
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		timer += Gdx.graphics.getDeltaTime();
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);

		//------ 入力
		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			game.setScreen(new TitleScreen(game));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
			fileIO.save();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F6)) {
		}
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			//-- ワールド座標に変換
			viewport.unproject(touchPos);
		}
		if(FlagManagement.is(Flag.LOOK_FREE)) {
			if (Gdx.input.isKeyPressed(Input.Keys.W) && zoom > 1) {
				zoom -= 0.1;
				camera.zoom -= 0.1;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				zoom += 0.1;
				camera.zoom += 0.1;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				zoom = 1.0f;
				setCameraPositionToTurnPlayer();
			}
		}

		if(FlagManagement.is(Flag.PLAY)) {
			// UIの動作
			if(FlagManagement.is(Flag.UI_INPUT_ENABLE)) ui.update();
			// シークエンスの動作
			sequence.getAsInt();
			//-- その他の動作
			playerManager.update();
			dice.update();
			board.update();
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
		if(FlagManagement.is(Flag.LOOK_PIECE)) setCameraPositionToTurnPlayer();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		// 塗りつぶし
		ScreenUtils.clear(0, 255, 0, 1);
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
		playerManager.draw(batch, renderer);

		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		if(FlagManagement.is(Flag.UI_VISIBLE)) ui.draw(batch, renderer, font);
		//-- デバッグ表示
		if(FlagManagement.is(Flag.PRINT_DEBUG_INFO)) {
			batch.begin();
			font.getData().setScale(1, 1);
			font.draw(batch, "ScreenOrigin: x:" + screenOrigin.x + " y:" + screenOrigin.y, 0, 18*0);
			font.draw(batch, "CameraPosition: x:" + camera.position.x + " y:" + camera.position.y+ " zoom:" + camera.zoom, 0, 18*1);
			font.draw(batch, "Sequence_no: " + sequenceNo, 0, 18*2);
			font.draw(batch, "FPS: " +Gdx.graphics.getFramesPerSecond() , 0, 18*3);
			font.draw(batch, "turn_player_no: " +turnPlayerNo , 0, 18*4);
			font.draw(batch, "goal_no: " +goalNo , 0, 18*5);
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
		playerManager.dispose();
		dice.dispose();
		board.dispose();
	}

	private int turnStandby() {
		if(sequenceNo == Sequence.TURN_STANDBY.no) {
			// 全員ゴールしたらリザルトへ
			if(playerManager.isAllGoal()) {
				sequenceNo = Sequence.RESULT.no;
				sequence = this::result;
				return 0;
			}
			do {
				turnPlayerNo++;
				if (turnPlayerNo >= playerManager.getSize()) {
					turnPlayerNo = 0;
					turnCount++;
				}
				turnPlayer = playerManager.getPlayer(turnPlayerNo);
			} while (turnPlayer.isGoal());
			ui.add(new UIPartsSelect("confirm_ready", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 0, true, turnPlayer.getName()+"の番です"));
			((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(turnCount+"ターン目");
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.TURN_STANDBY.no +1) {
			int select = ui.getSelect();
			if(select != -1 ) {
				sequenceNo = Sequence.ACTION_SELECT.no;
				sequence = this::actionSelect;
			}
		}

		return 0;
	}

	private int actionSelect() {
		if(sequenceNo == Sequence.ACTION_SELECT.no) {
			ui.add(new UIPartsSelect("action_select", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 0, true, "サイコロを振る", "マップ確認", "セーブ"));
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.ACTION_SELECT.no +1) {
		    int select = ui.getSelect();
			if(select != -1 ) {
				if (select == 0) {
					sequenceNo = Sequence.DICE_ROLL.no;
					sequence = this::diceRoll;
				}
				if (select == 1) {
					FlagManagement.set(Flag.LOOK_FREE);
					sequenceNo += 1;
				}
				if (select == 2) {
					sequenceNo += 2;
				}
			}
			return 0;
		}
		// マップ確認
		if(sequenceNo == Sequence.ACTION_SELECT.no +2) {
			if (FlagManagement.is(Flag.INPUT_ENABLE)) {
				if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(turnCount+"ターン目");
					zoom = 1.0f;
					FlagManagement.set(Flag.LOOK_PIECE);
					sequenceNo = Sequence.ACTION_SELECT.no;
				}
				if(FlagManagement.is(Flag.LOOK_FREE)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("方向キーでカメラ移動\nシフト押しながらで高速移動\n[Q]キーで全体マップ\n[S]キーで拡大\n[W]キーで縮小\n[R]キーでカメラリセット");
					freeCamera();
					if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
						zoom = mapCameraZoom;
						camera.zoom = mapCameraZoom;
						camera.position.x = mapCameraWidth;
						camera.position.y = mapCameraHeight;
						FlagManagement.set(Flag.LOOK_MAP);
					}
				} else if (FlagManagement.is(Flag.LOOK_MAP)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("[Q]キーで詳細マップ");
					if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
						zoom = 1.0f;
						setCameraPositionToTurnPlayer();
						FlagManagement.set(Flag.LOOK_FREE);
					}
				}
			}
		}

		if(sequenceNo == Sequence.ACTION_SELECT.no +3) {
			saveData.setGameState(timer, goalNo, sequenceNo, turnPlayerNo);
			fileIO.save();
			sequenceNo = Sequence.ACTION_SELECT.no;
		}

		return 0;
	}

	private int diceRoll() {
		if(sequenceNo == Sequence.DICE_ROLL.no) {
			dice.rollStart();
			sequenceNo++;
		}
		if(sequenceNo == Sequence.DICE_ROLL.no +1) {
			if (FlagManagement.is(Flag.INPUT_ENABLE) && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				turnPlayer.addADiceNo(dice.rollStop());
				sequenceNo = Sequence.PIECE_ADVANCE.no;
				sequence = this::PieceAdvance;
			}
		}

		return 0;
	}

	private int PieceAdvance() {
		if(sequenceNo == Sequence.PIECE_ADVANCE.no) {
			ui.add(new UIPartsSelect("move_piece", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 0, true, "移動"));
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.PIECE_ADVANCE.no +1) {
			int select = ui.getSelect();
			if(select != -1 ) {
				turnPlayer.getPiece().move(dice.getNo(), true);
				sequenceNo++;
			}
		}
		if(sequenceNo == Sequence.PIECE_ADVANCE.no +2) {
			if(!FlagManagement.is(Flag.PIECE_MOVE)) sequenceNo++;
		}
		if(sequenceNo == Sequence.PIECE_ADVANCE.no +3) {
			sequenceNo = Sequence.TASK_DO.no;
			sequence = this::taskDo;
		}

		return 0;
	}

	private int taskDo() {
		// ターンプレイヤーが居るマス
		Square visitSquare = board.getSquare(turnPlayer.getPiece().getSquareNo());
		if(sequenceNo == Sequence.TASK_DO.no) {
			turnPlayer.addResultDetail(visitSquare, turnCount);
			if(turnPlayer.isGoal()) {
				// ※ゴール演出へ
				((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("ゴール！");
				sequenceNo = Sequence.TASK_DO.no+2;
				return 0;
			}
			if(visitSquare.hasDocument()) ((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(visitSquare.getDocument());
			if(visitSquare.getType() == 4) {
				move = visitSquare.getMove();
				back = visitSquare.getBack();
				ui.add(new UIPartsSelect("task_result_check", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 0, true, "成功", "失敗"));
			} else if(visitSquare.getType() == 3) {
				move = visitSquare.getMove();
				ui.add(new UIPartsSelect("move_check", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 0, true, "移動"));
			} else {
				sequenceNo += 2;
				return 0;
			}
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.TASK_DO.no +1) {
			FlagManagement.set(Flag.PIECE_MOVE);
            int select = ui.getSelect();
			if(select != -1 ) {
				if (select == 0) turnPlayer.getPiece().move(move, true);
				if (select == 1){
					turnPlayer.getPiece().move(-back, true);
					turnPlayer.getResultDetail(turnCount).setTaskResult(false);
				}
				sequenceNo++;
			}
		}
		if(sequenceNo == Sequence.TASK_DO.no +2) {
			if(!FlagManagement.is(Flag.PIECE_MOVE)) {
				timerRap = timer;
				sequenceNo++;
			}
		}
		if(sequenceNo == Sequence.TASK_DO.no +3) {
			if(timer-timerRap >= 0.5f) sequenceNo++;
		}
		if(sequenceNo == Sequence.TASK_DO.no +4) {
			sequenceNo = Sequence.TURN_STANDBY.no;
			sequence = this::turnStandby;
		}

		return 0;
	}

	private int result() {
		if(sequenceNo == Sequence.RESULT.no) {
			StringBuilder txt = new StringBuilder("全員ゴールしたよ");
			Iterator<Player> playerIterator = new Array.ArrayIterator<>(playerManager.getGoalPlayer());
			while(playerIterator.hasNext()) {
				Player player = playerIterator.next();
				txt.append("\n").append(player.getGoalNo()).append("位:").append(player.getName());
			}
			((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(txt.toString());
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.RESULT.no +1) {
			timerRap = timer;
			sequenceNo++;
		}

		return 0;
	}

	private void setCameraPositionToTurnPlayer() {
		Vector2 pv = turnPlayer.getPiece().getPosition();
		camera.position.x = pv.x;
		camera.position.y = pv.y;
		camera.zoom = zoom;
	}

	private void freeCamera() {
		int x = 0;
		int y = 0;
		int fast = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) fast = 2;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x = -1;
		else if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) y = -1;
		else if(!Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) y = 1;
		if(x != 0 || y != 0) camera.translate(6*x*fast, 6*y*fast);
	}

	public int getGoalNo() {
		return goalNo;
	}

	public void addGoalNo() {
		goalNo++;
	}

	public int getTurnCount() {
		return turnCount;
	}
}
