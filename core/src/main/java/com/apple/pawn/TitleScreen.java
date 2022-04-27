package com.apple.pawn;

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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.function.IntSupplier;


public class TitleScreen implements Screen {
	private final static int DISTANCE = 260;
	private static float SPEED = 0.01f;

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

	private final Vector3 screenOrigin;
	private int sequenceNo;					// シークエンス番号
	private int sequenceSubNo;				// サブシークエンス番号
	private int dialogNo;					// 何人目の入力中か

	private final UI ui;							// UI
	private final FileIO fileIO;			// セーブファイルの読み書き

	private final ParticleManager particle;
	private final GameSetting gameSetting;

	private final AssetManager manager;

	private int playerNo;
	private double rad;

	//-- リソース
	private final BitmapFont fontTitle;
	private final Sprite[] spPiece;
	private final Sprite mainImage;


	/**
	 * コンストラクタ 初期化、読み込み
	 */
	public TitleScreen(@NonNull final Pawn game) {
		this.game = game;
		batch = game.batch;
		font = game.font;
		renderer = game.renderer;
		manager = game.manager;
		manager.update();
		manager.finishLoading();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		viewport = new FitViewport(Pawn.LOGICAL_WIDTH,Pawn.LOGICAL_HEIGHT,camera);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		uiViewport = new FitViewport(Pawn.LOGICAL_WIDTH,Pawn.LOGICAL_HEIGHT,uiCamera);

		screenOrigin = new Vector3();

		ui = new UI();
		ui.initialize(game);
		fileIO = new FileIO();

		particle = new ParticleManager();
		particle.startParticle(30);
		particle.skipFrame(60*10);
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
		manager.load("assets/title.png", Texture.class);
		manager.update();
		manager.finishLoading();
		spPiece = new Sprite[6];
		for(int i = 0; i < 6; i++) {
			spPiece[i] = atlas.createSprite(Piece.COLOR[i]);
			spPiece[i].flip(false, true);
			spPiece[i].setSize(80, 120);
		}
		mainImage = new Sprite(manager.get("assets/title.png", Texture.class),0,0,1024,591);
		mainImage.flip(false,true);
		mainImage.setSize(614,354);
		mainImage.setPosition((Pawn.LOGICAL_WIDTH >> 1)-307,Pawn.LOGICAL_HEIGHT-354-130);

		FlagManagement.set(Flag.PLAY);
		FlagManagement.set(Flag.UI_VISIBLE);
		FlagManagement.set(Flag.PRINT_DEBUG_INFO);
		FlagManagement.set(Flag.UI_INPUT_ENABLE);
		FlagManagement.set(Flag.INPUT_ENABLE);

		sequenceNo = 1;
		sequenceSubNo = 1;
		dialogNo = 1;
		sequence = this::homeSequence;
	}

	/**
	 * update 更新。メインループの描画以外。
	 */
	private void update() {
		screenOrigin.set(0,0,0);
		viewport.unproject(screenOrigin);

		//-- 左右で回る駒のスピード変更
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
				Gdx.input.isKeyPressed(Input.Keys.D)) {
			SPEED += SPEED/100;
			if(SPEED > 0.05f) SPEED = 0.05f;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
				Gdx.input.isKeyPressed(Input.Keys.A)) {
			SPEED -= SPEED/100;
			if(SPEED < 0.002f) SPEED = 0.002f;
		}

		particle.update(game.getTimer());
		manager.update();

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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		//-- 論理表示領域を黒で塗りつぶし
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(0.5f,0.1f,0.5f,1);
		renderer.rect(screenOrigin.x, screenOrigin.y, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
		//-- 背景の水玉模様
		renderer.setColor(0.6f,0.2f,0.6f,1);
		int x=-1, y=-1, distance = 100;
		boolean isOdd = true;
		while(true) {
			y++;
			if(y*distance > Pawn.LOGICAL_HEIGHT) break;

			while(true) {
				x++;
				if(x*distance > Pawn.LOGICAL_WIDTH) break;
				if(isOdd  &&  x%2 == 0)  continue;
				if(!isOdd  &&  x%2 == 1)  continue;
				renderer.circle(x*distance+34, y*distance+16, distance/1.8f);
			}

			isOdd = !isOdd;
			x = -1;
		}
		renderer.end();

		//------ 描画
		if(sequenceNo == 1  ||  sequenceNo == 2 || sequenceNo == 3) {
			batch.begin();
			mainImage.draw(batch);
			batch.end();

			int px= Pawn.LOGICAL_WIDTH >> 1, py= (Pawn.LOGICAL_HEIGHT >> 1);
			double tRad = rad, sa = Math.toRadians((double) 360/playerNo);
			batch.begin();
			for(int i=0 ; i<playerNo ; i++) {
				spPiece[i].setCenter(Math.round(Math.cos(tRad) * (DISTANCE+240) + px), Math.round(Math.sin(tRad) * DISTANCE + py));
				spPiece[i].draw(batch);
				tRad += sa;
			}
			batch.end();

		} else if(sequenceNo == 4) {

		}


		//------ ui描画
		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		renderer.setProjectionMatrix(uiCamera.combined);
		particle.draw(batch, renderer);
		if(sequenceNo != 4) {
			fontTitle.getData().setScale(1, 1);
			batch.begin();
			fontTitle.draw(batch, "すごろくゲーム", (Pawn.LOGICAL_WIDTH >> 1) - 329, (Pawn.LOGICAL_HEIGHT >> 1) - 210);
			batch.end();
		}
		ui.draw(batch, renderer, font, 1);
		font.getData().setScale(1, 1);
		batch.begin();
		font.draw(batch, "ScreenOrigin: x:" + screenOrigin.x + " y:" + screenOrigin.y, 0, 18*0);
		font.draw(batch, "CameraPosition: x:" + camera.position.x + " y:" + camera.position.y+ " zoom:" + camera.zoom, 0, 18*1);
		font.draw(batch, "Sequence_no: " + sequenceSubNo, 0, 18*2);
		font.draw(batch, "FPS: " +Gdx.graphics.getFramesPerSecond() , 0, 18*3);
		batch.end();
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
		particle.dispose();
	}

	private int homeSequence() {
		rad += SPEED;
		if(rad >360) rad %= 360;
		if(sequenceSubNo == 1) {
			String load = "続きから";
			if(fileIO.isExistsSaveData() == false) load = "/" + load;
			ui.add(new UIPartsSelect("title_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 1, 0, true, "開始", load, "実績", "終了"));
			sequenceSubNo++;
		}
		if(sequenceSubNo == 2) {
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
			// 終了
			if (select == 3) {
				Gdx.app.exit();
			}
		}
		return 0;
	}

	private int startSettingSequence() {
		rad += SPEED;
		if(rad >360) rad %= 360;
		if(sequenceSubNo == 1) {
			ui.add(new UIPartsSelectIndex("setting_menu", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 1, 2, true, "プレイ人数", "2人", "3人", "4人", "5人", "6人"));
			sequenceSubNo++;
		}
		// 人数
		if(sequenceSubNo == 2) {
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
				Gdx.app.debug("fps", "select="+select);
				playerNo = select+2;
				gameSetting.init(playerNo);
				sequence = this::startSetting2Sequence;
				sequenceNo = 3;
				sequenceSubNo = 1;
				dialogNo = 1;
			}
		}
		return 0;
	}

	private int startSetting2Sequence() {
		rad += SPEED;
		if(rad >360) rad %= 360;
		// 名前の入力
		if (sequenceSubNo == 1 && !FlagManagement.is(Flag.DIALOG_SHOW)) {
			if (dialogNo <= playerNo) {
				FlagManagement.set(Flag.DIALOG_SHOW);
				//-- 名前入力ダイアログ
				Gdx.input.getTextInput(new Input.TextInputListener() {
					@Override
					public void input(String text) {
						gameSetting.setAName(dialogNo - 1, text);
//						Gdx.app.debug("info", text);
						FlagManagement.fold(Flag.DIALOG_SHOW);
						dialogNo++;
					}

					@Override
					public void canceled() {
//						Gdx.app.debug("info", "canceled");
						FlagManagement.fold(Flag.DIALOG_SHOW);
						sequenceSubNo = 5;
					}
				}, dialogNo + "Pの名前", null, "名前を入力してください");
			} else {
				sequenceSubNo++;
			}
		}
		if (sequenceSubNo == 2) {
			ui.add(new UIPartsSelectIndex("stage_select", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 1, 0, true, "ステージ選択", "ステージ1","ステージ2","ステージ3"));
			sequenceSubNo++;
		}
		if (sequenceSubNo == 3) {
			int select = ui.getSelect();
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				ui.remove("stage_select");
				sequence = this::startSettingSequence;
				sequenceNo = 2;
				sequenceSubNo = 1;
				return 0;
			}
			if(select != -1 ) {
				gameSetting.setStageNo(select);
				ui.add(new UIPartsSelect("start_confirm", Pawn.LOGICAL_WIDTH / 2 - 150, 600, 300, 16, 1, 0, true, "開始！", "設定をやり直す"));
				sequenceSubNo++;
			}
		}
		if (sequenceSubNo == 4) {
			int select = ui.getSelect();
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				ui.remove("start_confirm");
				sequenceSubNo = 2;
				return 0;
			}
			if(select != -1 ) {
				if(select == 0) {
					GameScreen gameScreen = new GameScreen(game);
					gameScreen.initialize(gameSetting);
					game.setScreen(gameScreen);
				} else if(select == 1) {
					sequenceSubNo++;
				}
			}
		}
		if(sequenceSubNo == 5) {
			sequence = this::startSettingSequence;
			sequenceNo = 2;
			sequenceSubNo = 1;
			return 0;
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
			ui.add( new UIPartsAchievementView("achievement",50,30,1180,660, 1 ));
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
