package com.drop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {
    private final Game game;
    private final SpriteBatch batch;
    private final Texture gameOverTexture;
    private final Texture restartTexture;
    private final Texture background;
    private final float gameOverX;
    private final float gameOverY;
    private final float restartX;
    private final float restartY;
    Sound sound;

    public GameOverScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        gameOverTexture = new Texture(Gdx.files.internal("GameOver.png"));
        restartTexture = new Texture(Gdx.files.internal("Restart.png"));
        background = new Texture(Gdx.files.internal("space2.png"));

        gameOverX = 300 - gameOverTexture.getWidth() / 2;
        gameOverY = 500;
        restartX = 300 - restartTexture.getWidth() / 2;
        restartY = 300;
    }

    @Override
    public void show() {
        sound = Gdx.audio.newSound(Gdx.files.internal("start.mp3"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Draw the background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Draw the game over and restart textures
        batch.draw(gameOverTexture, gameOverX, gameOverY);
        batch.draw(restartTexture, restartX, restartY);
        batch.end();

        if (Gdx.input.isTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.input.getY();
            touchY = Gdx.graphics.getHeight() - touchY; // Convert to y-down coordinates

            if (touchX >= restartX && touchX <= restartX + restartTexture.getWidth()
                    && touchY >= restartY && touchY <= restartY + restartTexture.getHeight()) {
                game.setScreen(new GameScreen(game));
                sound.play();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // No additional actions required on resize
    }

    @Override
    public void pause() {
        // No additional actions required on pause
    }

    @Override
    public void resume() {
        // No additional actions required on resume
    }

    @Override
    public void hide() {
        // Dispose of resources when the screen is hidden
        batch.dispose();
        gameOverTexture.dispose();
        restartTexture.dispose();
        background.dispose();
    }

    @Override
    public void dispose() {
        // Dispose of resources when the screen is destroyed
        batch.dispose();
        gameOverTexture.dispose();
        restartTexture.dispose();
        background.dispose();
    }
}
