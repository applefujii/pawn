package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.function.IntSupplier;


public class TitleScreen implements Screen {
	private final static int DISTANCE = 240;
	private final static float SPEED = 0.02f;

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
	private int sequenceSubNo;				// サブシークエンス番号

	private UI ui;							// UI
	private final FileIO fileIO;			// セーブファイルの読み書き

	private GameSetting gameSetting;

	private final AssetManager manager;

	private int playerNo;
	private double rad;

	//-- リソース
	private final BitmapFont fontTitle;
	private final Sprite[] spPiece;


	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public TitleScreen(final Pawn game) {
		this.game = game;
		batch = game.batch;
		font = game.font;
		renderer = game.renderer;
		manager = game.manager;
		if(!manager.isLoaded("assets/piece_atlas.txt", TextureAtlas.class)) manager.load("assets/piece_atlas.txt", TextureAtlas.class);
		manager.update();
		manager.finishLoading();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		viewport = new FitViewport(Pawn.LOGICAL_WIDTH,Pawn.LOGICAL_HEIGHT,camera);
		uiViewport = new FitViewport(Pawn.LOGICAL_WIDTH,Pawn.LOGICAL_HEIGHT,camera);
		stage = new Stage(viewport);
		uiStage = new Stage(uiViewport);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		FitViewport uiViewport = new FitViewport(Pawn.LOGICAL_WIDTH,Pawn.LOGICAL_HEIGHT,uiCamera);
		stage = new Stage(uiViewport);

		screenOrigin = new Vector3();
		touchPos = new Vector3();

		ui = new UI();
		ui.initialize(game);
		fileIO = new FileIO();

		gameSetting = new GameSetting();
		playerNo = 6;
		rad = 0;

		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 94;
		param.incremental = true;			// 自動的に文字を追加
		param.color = Color.CORAL;			// 文字色
		param.borderColor = Color.BLACK;	// 境界線色
		param.borderWidth = 2;				// 境界線の太さ
		param.flip = true;					// 上下反転
		fontTitle = game.fontGenerator.generateFont(param);

		TextureAtlas atlas = manager.get("assets/piece_atlas.txt", TextureAtlas.class);
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

		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);

		sequenceNo = 1;
		sequenceSubNo = 1;
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
			gameSetting.init(4);
			GameScreen gameScreen = new GameScreen(game);
			gameScreen.initialize(gameSetting);
			game.setScreen(gameScreen);
		}

		if(FlagManagement.is(Flag.UI_INPUT_ENABLE)) ui.update();
		sequence.getAsInt();
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
		renderer.setColor(0.5f,0.1f,0.5f,1);
		renderer.rect(screenOrigin.x, screenOrigin.y,game.LOGICAL_WIDTH,game.LOGICAL_HEIGHT);
		renderer.end();

		//------ 描画
		if(sequenceNo == 1  ||  sequenceNo == 2) {
			batch.begin();
			int px=game.LOGICAL_WIDTH/2-40, py=game.LOGICAL_HEIGHT/2-60;
			double tRad = rad, sa = Math.toRadians(360/playerNo);
			for(int i=0 ; i<playerNo ; i++) {
				spPiece[i].setPosition((int) (Math.cos(tRad) * DISTANCE + px), (int) (Math.sin(tRad) * DISTANCE + py));
				spPiece[i].draw(batch);
				tRad += sa;
			}
			batch.end();

			fontTitle.getData().setScale(1, 1);
			batch.begin();
			fontTitle.draw(batch, "すごろくゲーム", game.LOGICAL_WIDTH / 2 - 329, game.LOGICAL_HEIGHT / 2 - 100);
			batch.end();
		} else if(sequenceNo == 3) {

		}

		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		ui.draw(batch, renderer, font);
		font.getData().setScale(1, 1);
		batch.begin();
		font.draw(batch, "ScreenOrigin: x:" + screenOrigin.x + " y:" + screenOrigin.y, 0, 16*0);
		font.draw(batch, "CameraPosition: x:" + camera.position.x + " y:" + camera.position.y+ " zoom:" + camera.zoom, 0, 16*1);
		font.draw(batch, "Sequence_no: " + sequenceSubNo, 0, 16*2);
		font.draw(batch, "FPS: " +Gdx.graphics.getFramesPerSecond() , 0, 16*3);
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

	private int homeSequence() {
		if(sequenceSubNo == 1) {
			ui.add(new UIPartsSelect("title_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 0, true, "開始", "続きから", "設定"));
			sequenceSubNo++;
		}
		if(sequenceSubNo == 2) {
			rad += SPEED;
			if(rad >360) rad %= 360;
			int select = ui.getSelect();
			// 開始
			if (select == 0) {
				sequence = this::startSettingSequence;
				sequenceNo = 2;
				sequenceSubNo = 1;
			}
			// 続きから
			if (select == 1) {
				sequence = this::loadSequence;
				sequenceNo = 3;
				sequenceSubNo = 1;
			}
			// 実績確認
			if (select == 2) {
				sequence = this::achievementViewSequence;
				sequenceNo = 4;
				sequenceSubNo = 1;
			}
		}
		return 0;
	}

	private int startSettingSequence() {
		if(sequenceSubNo == 1) {
			ui.add(new UIPartsSelectIndex("setting_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 2, true, "プレイ人数", "2人", "3人", "4人", "5人", "6人"));
			sequenceSubNo++;
		}
		// 人数
		if(sequenceSubNo == 2) {
			rad += SPEED;
			if(rad >360) rad %= 360;
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				ui.remove("setting_menu");
				sequence = this::homeSequence;
				sequenceNo = 1;
				sequenceSubNo = 1;
				return 0;
			}
			playerNo = ui.getCursor()+2;
			int select = ui.getSelect();
			if(select != -1 ) {
				playerNo = select+2;
				gameSetting.init(playerNo);
				sequence = this::startSetting2Sequence;
				sequenceNo = 3;
				sequenceSubNo = 1;
			}
		}
		return 0;
	}

	private int startSetting2Sequence() {
		if (sequenceSubNo == 1) {
			GameScreen gameScreen = new GameScreen(game);
			gameScreen.initialize(gameSetting);
			game.setScreen(gameScreen);
		}
		return 0;
	}

	private int loadSequence() {
		if (sequenceSubNo == 1) {
			fileIO.load();
			GameScreen gameScreen = new GameScreen(game);
			gameScreen.load(fileIO.getSaveData());
			game.setScreen(gameScreen);
		}
		return 0;
	}

	private int achievementViewSequence() {
		if (sequenceSubNo == 1) {
			ui.add( new UIPartsAchievementView("achievement",50,30,1180,660 ));
			sequenceSubNo++;
		}
		if (sequenceSubNo == 2) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				ui.remove("achievement");
				sequence = this::homeSequence;
				sequenceNo = 1;
				sequenceSubNo = 1;
				return 0;
			}
		}
		return 0;
	}

}
