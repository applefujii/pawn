package com.apple.pawn.lwjgl3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;

public class ExtendedLwjgl3Application extends Lwjgl3Application {
    public ExtendedLwjgl3Application(ApplicationListener listener, Lwjgl3ApplicationConfiguration config) {
        super(listener, config);
    }

    @Override
    public Lwjgl3Input createInput(Lwjgl3Window window) {
        return new PawnLwjgl3Input(window);
    }
}
