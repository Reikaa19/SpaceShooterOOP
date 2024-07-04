package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class Bullet extends Enemy {
    private int speed;
    private  int damage;
    private int angle;
    private float bulletX;
    private float bulletY;
    private float bulletVelocityX;
    private float bulletVelocityY;

    public Bullet(){}

    public Bullet(String imgAsset, int width, int height,int damage,int speed) {
        setImgAsset(imgAsset);
        setImage(width, height);
        setAngle(angle);
        setRotation(getAngle());
        setDamage(damage);
        setSpeed(speed);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public float getBulletX() {
        return bulletX;
    }

    public void setBulletX(float bulletX) {
        this.bulletX = bulletX;
    }

    public float getBulletY() {
        return bulletY;
    }

    public void setBulletY(float bulletY) {
        this.bulletY = bulletY;
    }

    public float getBulletVelocityX() {
        return bulletVelocityX;
    }

    public void setBulletVelocityX(float bulletVelocityX) {
        this.bulletVelocityX = bulletVelocityX;
    }

    public float getBulletVelocityY() {
        return bulletVelocityY;
    }

    public void setBulletVelocityY(float bulletVelocityY) {
        this.bulletVelocityY = bulletVelocityY;
    }
}

class scoutBullet extends Bullet {
    scoutBullet() {
        super ("Bullet-down-small.gif", 10, 10,1,300);
    }
    @Override
    public void setImage(int width, int height) {
        super.setImage(10, 16);
    }
}

class bomberBullet extends Bullet {
    public bomberBullet() {
        super("Bullet-Bomb.gif", 36, 36,5,200);
    }

}
