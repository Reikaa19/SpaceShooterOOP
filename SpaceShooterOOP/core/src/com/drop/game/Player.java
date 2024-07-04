package com.drop.game;

public class Player extends Entity{
    private int score;

    public Player(String imgAsset, int xCoord, int yCoord, int width, int height, int hp) {
        this.setImgAsset(imgAsset);
        this.setHitbox(xCoord,yCoord,width,height);
        this.setHp(hp);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
//--------------------------------------------------------------------------------------------

