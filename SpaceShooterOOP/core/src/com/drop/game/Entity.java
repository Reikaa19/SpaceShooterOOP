package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Entity {
    private Texture imgAsset;
    private Sprite image;
    private Rectangle hitbox;




    public Entity() {
    }

    public int getxCoord() {
        return (int)this.hitbox.getX();
    }

    public int getyCoord() {
        return (int)this.hitbox.getY();
    }

    public void hitboxCheck(){
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();
    }


    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(int x, int y, int width, int height) {
        this.hitbox = new Rectangle();
        hitbox.x = x;        // position
        hitbox.y = y;

        hitbox.width = width;    // size in pixel
        hitbox.height = height;
    }

    public Sprite getImage() {
        return image;
    }

    public void setImage(int width, int height) {
        this.image = new Sprite(this.imgAsset,0,0,width,height);
    }

    public void setPosition(int x, int y){
        this.image.setPosition(x,y);
    }

    public void setRotation(int degree){
        this.image.setRotation(degree);
    }

    public void draw(SpriteBatch batch){
        image.draw(batch);
    }
    public Texture getImgAsset() {
        return imgAsset;
    }

    public void setImgAsset(String filePath) {
        this.imgAsset = new Texture((Gdx.files.internal(filePath)));
    }

}
