package com.drop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.drop.game.GameScreen;

public class GameOverScreen extends ScreenAdapter {
    private final Game game;
    private final SpriteBatch batch;
    private final Texture gameOverTexture;
    private final Texture restartTexture;
    private final float gameOverX;
    private final float gameOverY;
    private final float restartX;
    private final float restartY;

    public GameOverScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        gameOverTexture = new Texture(Gdx.files.internal("GameOver.png"));
        restartTexture = new Texture(Gdx.files.internal("Restart.png"));
        gameOverX = 300 - gameOverTexture.getWidth() / 2;
        gameOverY = 500;
        restartX = 300 - restartTexture.getWidth() / 2;
        restartY = 200;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
            }
        }
    }

    @Override
    public void hide() {
        batch.dispose();
        gameOverTexture.dispose();
        restartTexture.dispose();
    }
}
