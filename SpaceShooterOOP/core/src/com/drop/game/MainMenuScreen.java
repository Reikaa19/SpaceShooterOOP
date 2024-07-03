package com.drop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    private Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture playButton;
    private Texture exitButton;
    private Texture title;
    private Texture background;
    Sound sound;


    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 900);
        batch = new SpriteBatch();
        playButton = new Texture(Gdx.files.internal("play.png"));
        exitButton = new Texture(Gdx.files.internal("exit.png"));
        title = new Texture(Gdx.files.internal("title.png"));
        background = new Texture(Gdx.files.internal("space.jpg"));
        sound = Gdx.audio.newSound(Gdx.files.internal("start.mp3"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, 600, 900);
        batch.draw(title, 300 - title.getWidth() / 2, 600 - title.getHeight() / 2);
        batch.draw(playButton, 300 - playButton.getWidth() / 2, 400 - playButton.getHeight() / 2);
        batch.draw(exitButton, 300 - exitButton.getWidth() / 2, 300 - exitButton.getHeight() / 2);
        batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Check if play button is touched
            if (touchPos.x > 300 - playButton.getWidth() / 2 && touchPos.x < 300 + playButton.getWidth() / 2 &&
                    touchPos.y > 400 - playButton.getHeight() / 2 && touchPos.y < 400 + playButton.getHeight() / 2) {
                game.setScreen(new GameScreen(game));
                sound.play();
                dispose();
            }

            // Check if exit button is touched
            if (touchPos.x > 300 - exitButton.getWidth() / 2 && touchPos.x < 300 + exitButton.getWidth() / 2 &&
                    touchPos.y > 300 - exitButton.getHeight() / 2 && touchPos.y < 300 + exitButton.getHeight() / 2) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        playButton.dispose();
        exitButton.dispose();
        title.dispose();
        background.dispose();
        batch.dispose();
    }
}
