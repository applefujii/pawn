package com.apple.pawn.lwjgl3;

import com.apple.pawn.Pawn;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Lwjgl3Launcher {
	public static void main (String[] arg) {
		Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("ポーン");

		//---- 開発用
		config.setWindowedMode(1280, 720);
		config.useVsync(true);

		//---- リリース用
//		config.setWindowedMode(displayMode.width, displayMode.height);
//		config.setWindowPosition(0,0);
//		config.useVsync(true);
//		config.setResizable(false);
//		config.setDecorated(false);

		new ExtendedLwjgl3Application(new Pawn(), config);
	}
}
