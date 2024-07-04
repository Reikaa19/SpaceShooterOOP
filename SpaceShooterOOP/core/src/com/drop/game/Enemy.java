package com.drop.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity{
    public Enemy(){
    }

    public Enemy(String imgAsset, int xCoord, int yCoord, int width, int height, int hp) {
        this.setImgAsset(imgAsset);
        this.setHitbox(xCoord,yCoord,width,height);
        this.setImage(width, height);
        this.setPosition(xCoord,yCoord);
        this.setMaxHp(hp);
        this.setHp(hp);
    }

    public void collusionCheck(Player player, Sound enemyKilled, SpriteBatch batch) {
        if (player.getHitbox().overlaps(getHitbox())) {
            player.setHp(player.getHp() - getHp());
            System.out.println("Player hp: " + player.getHp());
            setHp(0);
            deadCheck(batch, enemyKilled);
        }
    }

    public void enemyHitCheck(Player player, Rectangle shot, Sound enemyKilled, SpriteBatch batch, Array<Rectangle> bulletShots) {
        if (getHitbox().overlaps(shot)) {
            setHp(getHp() - 1);
            if (getHp() <= 0) {
                player.setScore(player.getScore() + 10);
                System.out.println("Score: " + player.getScore());
                deadCheck(batch, enemyKilled);
            }
            bulletShots.removeValue(shot, true); // Remove bullet yg kena
        }
    }

    public int getMiddleX() {
        return (int) getImage().getX() + getImage().getRegionWidth() / 2;
    }

    public int getMiddleY() {
        return (int) getImage().getY() + getImage().getRegionHeight() / 2;
    }

    public int lockToPlayer(Player player) {
        double test = Math.atan2(getxCoord() - player.getxCoord() + 2, getyCoord() - player.getyCoord() + 2);
        return (int) Math.toDegrees(test) * -1;
    }

    // todo : enemy movement
    public void enemyMove(SpriteBatch batch, int x, int y, int width, int height) {
        setPosition(x, y);
        setHitbox(x, y, width, height);
        draw(batch);
    }

    public void enemyMoveShoot(SpriteBatch batch) {
        int randomX = MathUtils.random(0, 600 - getImage().getRegionWidth());
        if (getyCoord() < -90) {
            enemyMove(batch, randomX, 900, getImage().getRegionWidth(), getImage().getRegionHeight());
        } else {
            enemyMove(batch, getxCoord(), getyCoord() - 2, getImage().getRegionWidth(), getImage().getRegionHeight());
        }
    }

    public void deadCheck(SpriteBatch batch, Sound enemyKilled) {
        int randomX = MathUtils.random(0, 600 - getImage().getRegionWidth());
        if (getHp() <= 0) {
            enemyMove(batch, randomX, 900, getImage().getRegionWidth(), getImage().getRegionHeight());
            setHp(getMaxHp());
            enemyKilled.play();
        }
    }

}

class Scout extends Enemy{
    public Scout(int xCoord) {
        super("Scout_Engine.gif", xCoord - 24, 900, 48, 68, 5);
    }
}

class Bomber extends Enemy{
    public Bomber(int xCoord) {
        super("Frigate_Engine.gif", xCoord - 42, 900, 84, 92, 10);
        setRotation(180);
    }

    @Override
    public void setHitbox(int x, int y, int width, int height) {
        super.setHitbox(x, y, 84, 92);
    }
}


