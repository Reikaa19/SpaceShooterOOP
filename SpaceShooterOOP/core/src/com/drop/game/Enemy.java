package com.drop.game;

import com.badlogic.gdx.graphics.Texture;

public class Enemy extends Entity{
    private Bullet bullet;
    private int fireRate;

    public Enemy(){

    }

    public Enemy(String imgAsset, int xCoord, int yCoord, int width, int height, int hp) {
        this.setImgAsset(imgAsset);
        this.setHitbox(xCoord,yCoord,width,height);
        this.setImage(width, height);
        this.setPosition(xCoord,yCoord);
        this.setMaxHp(hp);
    }




    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

}

