package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

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



    public void trajectoryBullet(Enemy enemy, Bullet myBullet, int angle){
        setImgAsset(String.valueOf(myBullet.getImgAsset()));
        setHitbox(enemy.getMiddleX() - (int) myBullet.getImage().getWidth()/2, enemy.getMiddleY() , (int) myBullet.getImage().getWidth(), (int) myBullet.getImage().getHeight());
        setImage((int) myBullet.getImage().getWidth(), (int) myBullet.getImage().getHeight());
        setPosition((int) (enemy.getMiddleX() - myBullet.getImage().getWidth()/2), (int) (enemy.getMiddleY() - myBullet.getImage().getHeight()/2));
        setDamage(myBullet.getDamage());

        setAngle(angle);
        setRotation(getAngle());

        float radian = (float) Math.toRadians(angle - 90);
        setBulletVelocityX((float) Math.cos(radian) * myBullet.getSpeed());
        setBulletVelocityY((float) Math.sin(radian) * myBullet.getSpeed());

        setBulletX(enemy.getMiddleX() - 5);
        setBulletY(enemy.getMiddleY() - 5);
    }

    public void bulletMovement(Array<Bullet> bulletDrops,int i,Player player,SpriteBatch batch, Sound playerHit){
        setBulletX(getBulletX() + getBulletVelocityX() * Gdx.graphics.getDeltaTime());
        setBulletY(getBulletY() + getBulletVelocityY() * Gdx.graphics.getDeltaTime());

        int x = (int) getBulletX();
        int y = (int) getBulletY();

        enemyMove(batch, x, y, (int) getHitbox().getWidth(), (int) getHitbox().getHeight());

        if (getyCoord() + 20 < 0) bulletDrops.removeIndex(i);

        if (getHitbox().overlaps(player.getHitbox())) {
            bulletDrops.removeIndex(i);
            player.setHp(player.getHp() - getDamage());
            System.out.println("Player hp: " + player.getHp());
            playerHit.play();
        }
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
