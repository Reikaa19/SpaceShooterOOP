package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity{
    private int score;

    public Player() {
        this.setImgAsset("PlayerShip.png");
        this.setHitbox(270, 30, 45, 45);
        this.setHp(20);
    }

    public boolean isGameOver(){
        if (getHp() <= 0) {
            System.out.println("Game Over");
            return true;
        }
        return false;
    }

    public void playerMovement(OrthographicCamera camera){
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set((Gdx.input.getX()), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            getHitbox().x = (int) (touchPos.x - 50 / 2);
            getHitbox().y = (int) (touchPos.y - 50 / 2);
        }
    }

    public void playerMoveRestriction(){
        if (getHitbox().x < 10) getHitbox().x = 10;
        if (getHitbox().x > 600 - 54) getHitbox().x = 600 - 54;
        if (getHitbox().y < 10) getHitbox().y = 10;
        if (getHitbox().y > 900 - 64) getHitbox().y = 900 - 64;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
//--------------------------------------------------------------------------------------------

