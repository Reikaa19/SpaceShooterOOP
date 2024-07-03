package com.drop.game;

public class Bullet extends Enemy{
    private int angle;
    private float bulletX;
    private float bulletY;
    private float bulletVelocityX;
    private float bulletVelocityY;

    public Bullet() {

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
