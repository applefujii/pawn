package com.apple.pawn;

import static com.apple.pawn.PawnUtils.median;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

	private final AssetManager manager;

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
	private String order;
	private final Sprite cursor;
	private boolean isLoad;					// 続きからのゲームか


	//---- 他のクラス
	private GameSetting gameSetting;				// ゲームの設定
	private final PlayerManager playerManager;		// プレイヤー管理
	private final BoardSurface board;				// 盤面
	private final Dice dice;						// さいころ
	private final UI ui;							// UI
	private final ParticleManager particle;			// パーティクル
	private final FileIO fileIO;					// セーブファイルの読み書き
	private final SaveData saveData;				// セーブファイル
	//private Result result;

	//---- 参照
	private Player turnPlayer;				// 現在のターンのプレイヤーを指す

	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public GameScreen (@NonNull final Pawn game) {
		this.game = game;
		batch = game.batch;
		font = game.font;
		renderer = game.renderer;
		manager = game.manager;
		timer = 0;
		goalNo = 0;
		sequenceNo = 1;
		turnPlayerNo = 1;
		zoom = 1.0f;
		mapCameraZoom = (float) BoardSurface.MAP_HEIGHT / Pawn.LOGICAL_HEIGHT;
		mapCameraHeight = (float) BoardSurface.MAP_HEIGHT / 2;
		mapCameraWidth = (float) BoardSurface.MAP_WIDTH / 2;
		turnCount = 1;
		isLoad = false;

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
		particle = new ParticleManager();
		fileIO = new FileIO();
		saveData = new SaveData();
		//-- 初期化
		manager.load("assets/map_atlas.txt", TextureAtlas.class);
		manager.load("assets/ui_atlas.txt", TextureAtlas.class);
		manager.load("assets/dice.png", Texture.class);
		manager.load("assets/back.png", Texture.class);
		manager.load("assets/cursor.png", Texture.class);
		manager.load("assets/black.png", Texture.class);
		manager.update();
		manager.finishLoading();
		dice.initialize(manager);
		playerManager.initialize(this);
		turnPlayerNo = -1;
		ui.initialize(game);
		//result.initialize(playerManager);
		cursor = new Sprite(manager.get("assets/cursor.png", Texture.class));
		cursor.flip(false, true);
		cursor.setCenter(Pawn.LOGICAL_WIDTH >> 1, Pawn.LOGICAL_HEIGHT >> 1);
		//-- 参照セット
		playerManager.setBoardSurface(board);
		ui.setDice(dice);
		fileIO.setSaveData(saveData);
		saveData.aPlayer = playerManager.getAPlayer();
		game.achievement.initialize(manager, ui, font);
		//-- 作成
		ui.add(new UIPartsExplanation(UI.SQUARE_EXPLANATION, manager, font, Pawn.LOGICAL_WIDTH-310, 100, 300, 360, 1, "マスの説明。折り返しできるようにしないとはみ出る。改行するとバグるので修正が必要。\n(追記)改行文字で改行可能に。"));
		ui.add(new UIPartsOperatingMethod(UI.OPERATING_METHOD, "操作説明欄"));
		// フラグ初期化
		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.UI_GROUP1_VISIBLE);
		FlagManagement.set(Flag.UI_GROUP2_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.DEBUG_CONTROL);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);
		FlagManagement.set(Flag.LOOK_PIECE);

		sequenceNo = Sequence.TURN_STANDBY.no;
		// 動作させる関数を代入
		sequence = this::turnStandby;
	}

	public void initialize(@NonNull final GameSetting setting) {
		this.gameSetting = setting;
		board.initialize(manager, setting.getStageNo(), font);
		String[] name = gameSetting.getAName();
		//Gdx.app.debug("fps", "name="+name);
		int[] color = gameSetting.getAColorNo();
		for(int i=0 ; i<name.length ; i++) {
			playerManager.add(name[i], color[i]);
		}

		StringBuilder orderBuilder = new StringBuilder();
		Iterator<Player> aPlayerIterator = new Array.ArrayIterator<>(playerManager.getAPlayer());
		while(aPlayerIterator.hasNext()) {
			Player player = aPlayerIterator.next();
			orderBuilder.append(player.getName());
			if(aPlayerIterator.hasNext()) orderBuilder.append("→");
		}
		order = orderBuilder.toString();
	}

	public void load(@NonNull final SaveData sd) {
		isLoad = true;
		gameSetting = new GameSetting();
		gameSetting.setStageNo(sd.mapNo);
		board.initialize(manager,sd.mapNo,font);
		playerManager.load(sd.aPlayer);
		timer = sd.timer;
		goalNo = sd.goalNo;
//		sequenceNo = sd.sequenceNo;
		turnPlayerNo = sd.turnPlayerNo-1;
		turnCount = sd.turnCount;
		saveData.aPlayer = playerManager.getAPlayer();

		StringBuilder orderBuilder = new StringBuilder();
		Iterator<Player> aPlayerIterator = new Array.ArrayIterator<>(playerManager.getAPlayer());
		while(aPlayerIterator.hasNext()) {
			Player player = aPlayerIterator.next();
			orderBuilder.append(player.getName());
			if(aPlayerIterator.hasNext()) orderBuilder.append("→");
		}
		order = orderBuilder.toString();
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		timer = game.getTimer();
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);
		manager.update();
		particle.update(game.getTimer());

		//------ 入力
		if(FlagManagement.is(Flag.DEBUG_CONTROL)) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
				game.setScreen(new TitleScreen(game));
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
				saveData.setGameState(timer, gameSetting.getStageNo(), goalNo, sequenceNo, turnPlayerNo, turnCount);
				fileIO.save();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
				fileIO.load();
				GameScreen gameScreen = new GameScreen(game);
				gameScreen.load(fileIO.getSaveData());
				game.setScreen(gameScreen);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			if(FlagManagement.is(Flag.PLAY)) {
				FlagManagement.fold(Flag.PLAY);
				ui.add(new UIPartsSelect("pause", Pawn.LOGICAL_WIDTH/2-150, Pawn.LOGICAL_HEIGHT/2-16, 300, 16, 2, 0, true, "再開", "終了"));
			}
			else {
				FlagManagement.set(Flag.PLAY);
				ui.remove("pause");
			}
		}
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			//-- ワールド座標に変換
			viewport.unproject(touchPos);
		}
		if(FlagManagement.is(Flag.LOOK_FREE)) {
			((UIPartsOperatingMethod)ui.getUIParts(UI.OPERATING_METHOD)).setDocument("方向キーでカメラ移動\n左シフト押しながらで高速移動\n[M]キーで全体マップ\n[E]キーで拡大\n[Q]キーで縮小\n[R]キーでカメラリセット\n[Space]キーで戻る");
		} else if(FlagManagement.is(Flag.LOOK_MAP)) {
			((UIPartsOperatingMethod)ui.getUIParts(UI.OPERATING_METHOD)).setDocument("[M]キーで詳細マップ\n[Space]キーで戻る");
		} else {
			((UIPartsOperatingMethod) ui.getUIParts(UI.OPERATING_METHOD)).setDocument("上下キーで選択\n[Space]キーで決定");
		}

		// UIの動作
		if(FlagManagement.is(Flag.UI_INPUT_ENABLE)) ui.update();
		//-- ポーズしていない
		if(FlagManagement.is(Flag.PLAY)) {
			// シークエンスの動作
			sequence.getAsInt();
			//-- その他の動作
			playerManager.update();
			dice.update();
			board.update(this);
		}
		//-- ポーズ中
		else {
			int select = ui.getSelect();
			if(select != -1 ) {
				if (select == 0) {
					FlagManagement.set(Flag.PLAY);
					ui.remove("pause");
				} else if (select == 1) {
					Gdx.app.exit();
				}
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
		if(FlagManagement.is(Flag.LOOK_PIECE)) {
		    setCameraPositionToTurnPlayer();
        }
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		// 塗りつぶし
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(0, 0, 0, 1);

		//------ 描画
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		for( int i=0 ; i<20 ; i++ ) {
			renderer.box(i*100, 0, 0, 1, 2000, 0);
			renderer.box(0, i*100, 0, 2000, 1, 0);
		}
		renderer.end();

		//------ メイン描画
		board.draw(batch);
		playerManager.draw(batch, renderer);
		particle.draw(batch, renderer);
		//if(FlagManagement.is(Flag.RESULT_SHOW)) result.draw(batch, renderer);

		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		if(FlagManagement.is(Flag.LOOK_FREE)) {
			batch.begin();
			cursor.draw(batch);
			batch.end();
		}
		if(FlagManagement.is(Flag.UI_VISIBLE)  &&  FlagManagement.is(Flag.UI_GROUP1_VISIBLE))
			ui.draw(batch, renderer, font, 1);
		//-- ポーズ中は暗くする
		if(FlagManagement.is(Flag.PLAY) == false) {
			Sprite black = new Sprite(manager.get("assets/black.png", Texture.class),0,0,32,32);
			batch.begin();
			black.setSize(Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
			black.setPosition(0, 0);
			black.setAlpha(0.6f);
			black.draw(batch);
			batch.end();
		}
		if(FlagManagement.is(Flag.UI_VISIBLE)  &&  FlagManagement.is(Flag.UI_GROUP2_VISIBLE))
			ui.draw(batch, renderer, font, 2);
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
	 * @param width 変更後のウィンドウの横幅
	 * @param height　変更後のウィンドウの縦幅
	 */
	@Override
	public void resize(int width, int height) {
		// ビューポートの更新
		viewport.update(width, height);
		uiViewport.update(width, height);
	}

	@Override
	public void show() { }

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	/**
	 * dispose 解放
	 */
	@Override
	public void dispose () {
		playerManager.dispose();
		dice.dispose();
		board.dispose();
		particle.dispose();
		//result.dispose();
		manager.unload("assets/map_atlas.txt");
		manager.unload("assets/ui_atlas.txt");
		manager.unload("assets/dice.png");
		manager.unload("assets/back.png");
		manager.unload("assets/cursor.png");
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
				//Gdx.app.debug("fps", "turnPlayer="+turnPlayer);
			} while (turnPlayer.isGoal());
			ui.add(new UIPartsSelect("confirm_ready", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, turnPlayer.getName()+"の番です"));
			((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(order+"\n"+turnCount+"ターン目");
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
			ui.add(new UIPartsSelect("action_select", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, "サイコロを振る", "マップ確認", "セーブ"));
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
			Gdx.app.debug("info", ""+FlagManagement.is(Flag.INPUT_ENABLE));
			if (FlagManagement.is(Flag.INPUT_ENABLE)) {
				if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(order+"\n"+turnCount+"ターン目");
					zoom = 1.0f;
					FlagManagement.set(Flag.LOOK_PIECE);
					sequenceNo = Sequence.ACTION_SELECT.no;
				}
				if(FlagManagement.is(Flag.LOOK_FREE)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("カーソルを合わせるとマスの情報が確認できます");
					freeCamera();
					if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
						zoom = mapCameraZoom;
						camera.zoom = mapCameraZoom;
						camera.position.x = mapCameraWidth;
						camera.position.y = mapCameraHeight;
						FlagManagement.set(Flag.LOOK_MAP);
					}
				} else if (FlagManagement.is(Flag.LOOK_MAP)) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("全体マップ");
					if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
						zoom = 1.0f;
						setCameraPositionToTurnPlayer();
						FlagManagement.set(Flag.LOOK_FREE);
					}
				}
			}
		}

		if(sequenceNo == Sequence.ACTION_SELECT.no +3) {
			saveData.setGameState(timer, gameSetting.getStageNo(), goalNo, sequenceNo, turnPlayerNo, turnCount);
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
			    int diceNumber = dice.rollStop();
				turnPlayer.addADiceNo(diceNumber);
				sequenceNo = Sequence.PIECE_ADVANCE.no;
				sequence = this::PieceAdvance;
			}
		}

		return 0;
	}

	private int PieceAdvance() {
		if(sequenceNo == Sequence.PIECE_ADVANCE.no) {
			ui.add(new UIPartsSelect("move_piece", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, "移動"));
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
			turnPlayer.addResultDetail(visitSquare);
			if(visitSquare.hasDocument()) ((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(visitSquare.getDocument());
			if(visitSquare.getType() == 4) {
				move = visitSquare.getMove();
				back = visitSquare.getBack();
				ui.add(new UIPartsSelect("task_result_check", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, "成功", "失敗"));
			} else if(visitSquare.getType() == 3) {
				move = visitSquare.getMove();
				ui.add(new UIPartsSelect("move_check", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, "移動"));
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
				if (select == 1)turnPlayer.getPiece().move(-back, true);
				sequenceNo++;
			}
		}
		if(sequenceNo == Sequence.TASK_DO.no +2) {
			if(!FlagManagement.is(Flag.PIECE_MOVE)) {
				if(turnPlayer.isGoal()) {
					((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation("ゴール！");
					ui.add(new UIPartsPopup("test", manager, font, Pawn.LOGICAL_WIDTH/2-150,100,300,100, 1, turnPlayer.getName()+"がゴール！\n"+goalNo+"位", 2));
					Vector2 pPos = turnPlayer.getPiece().getPosition().cpy();
					pPos.x += 40;
					pPos.y += 80;
					for(int i=0 ; i<30 ; i++)
						particle.addParticle(new ParticleInjectionFlutterDrop(
								pPos,
								(float) Math.toRadians(240),
								(float) Math.toRadians(30),
								12.0f
						));
					for(int i=0 ; i<30 ; i++)
						particle.addParticle(new ParticleInjectionFlutterDrop(
								pPos,
								(float) Math.toRadians(300),
								(float) Math.toRadians(30),
								12.0f
						));
					sequenceNo++;
				}
				timerRap = timer;
				sequenceNo++;
			}
		}
		if(sequenceNo == Sequence.TASK_DO.no +3) {
			if(timer-timerRap >= 0.5f) sequenceNo+=2;
		}
		if(sequenceNo == Sequence.TASK_DO.no +4) {
			if(timer-timerRap >= 3.0f) sequenceNo++;
		}
		if(sequenceNo == Sequence.TASK_DO.no +5) {
			// ゲーム情報の更新
			game.achievement.update(timer, turnPlayer);
			sequenceNo = Sequence.TURN_STANDBY.no;
			sequence = this::turnStandby;
		}

		return 0;
	}

	private int result() {
		if(sequenceNo == Sequence.RESULT.no) {
			if(isLoad) {
				fileIO.delete();
			}
			StringBuilder txt = new StringBuilder("全員ゴールしたよ");
			Iterator<Player> playerIterator = new Array.ArrayIterator<>(playerManager.getGoalPlayer());
			while(playerIterator.hasNext()) {
				Player player = playerIterator.next();
				txt.append("\n").append(player.getGoalNo()).append("位:").append(player.getName());
			}
			((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(txt.toString());
			ui.add(new UIPartsSelect("move_result", Pawn.LOGICAL_WIDTH/2-150, 600, 300, 16, 1, 0, true, "リザルト画面へ"));
			sequenceNo++;
			return 0;
		}
		if(sequenceNo == Sequence.RESULT.no +1) {
			FlagManagement.set(Flag.RESULT_SHOW);
			int select = ui.getSelect();
			if(select != -1 ) {
				//Gdx.app.debug("fps", "aSquareNo[0]="+aSquareNo[0]);
				Result result = new Result("result",50,50,Pawn.LOGICAL_WIDTH-100,Pawn.LOGICAL_HEIGHT-100,2,game);
				result.initialize(playerManager);
				ui.add(result);
			}
			timerRap = timer;
		}

		return 0;
	}

	private void setCameraPositionToTurnPlayer() {
		Vector2 pv = turnPlayer.getPiece().getCameraPosition();
		camera.position.x = pv.x+ (Piece.WIDTH >> 1);
		camera.position.y = pv.y+ (Piece.HEIGHT >> 1);
		camera.zoom = zoom;
	}

	/**
	 * Flag.LOOK_FREEが立っているときのカメラの操作
	 */
	private void freeCamera() {
		float m = 8;	//通常時の速さ(px/f)
		float x = 0;
		float y = 0;
		int fast = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) fast = 2;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
				Gdx.input.isKeyPressed(Input.Keys.A)) x -= m*fast;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
				Gdx.input.isKeyPressed(Input.Keys.D)) x += m*fast;
		if(Gdx.input.isKeyPressed(Input.Keys.UP) ||
				Gdx.input.isKeyPressed(Input.Keys.W)) y -= m*fast;
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
				Gdx.input.isKeyPressed(Input.Keys.S)) y += m*fast;
		x = median(x, -camera.position.x, BoardSurface.MAP_WIDTH - camera.position.x);
		y = median(y, -camera.position.y, BoardSurface.MAP_HEIGHT - camera.position.y);
		if(x != 0 || y != 0) camera.translate(x, y);
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			zoom -= 0.1;
			if(zoom < 1) zoom = 1.0f;
			camera.zoom = zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoom += 0.1;
			if(zoom > 5) zoom = 5.0f;
			camera.zoom = zoom;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			zoom = 1.0f;
			setCameraPositionToTurnPlayer();
		}
	}

	public AssetManager getManager() {
		return manager;
	}

	public Player getTurnPlayer() {
		return turnPlayer;
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

	public Vector3 getCameraPos() {
		return camera.position;
	}

	public void setUIPartsExplanation(String text) {
		((UIPartsExplanation)ui.getUIParts(UI.SQUARE_EXPLANATION)).setExplanation(text);
	}
}
