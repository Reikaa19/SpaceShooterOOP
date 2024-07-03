package com.drop.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.drop.game.MyGdxGame;

import javax.swing.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// display fps
		config.setForegroundFPS(120);

		// set window name
		config.setTitle("Space Shooter");

		// set window size
		config.setWindowedMode(600,900);

		// window icon
		config.setWindowIcon(Files.FileType.Internal, "PlayerShip.png");
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
