package com.drop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseScreen implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture playButton;
    private Texture pausedText;
    private OrthographicCamera camera;
    private Music bgm;

    public PauseScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        playButton = new Texture(Gdx.files.internal("play.png"));
        pausedText = new Texture(Gdx.files.internal("GamePaused.png"));
        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgmSS.mp3"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 900);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(pausedText, 300 - pausedText.getWidth() / 2, 600 - pausedText.getHeight() / 2);
        batch.draw(playButton, 300 - playButton.getWidth() / 2, 350 - playButton.getHeight() / 2);
        batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (touchPos.x > 300 - playButton.getWidth() / 2 && touchPos.x < 300 + playButton.getWidth() / 2
                    && touchPos.y > 350 - playButton.getHeight() / 2 && touchPos.y < 350 + playButton.getHeight() / 2) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        playButton.dispose();
        pausedText.dispose();
    }
}
