package com.drop.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;


//import java.awt.*;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;        // utk display gambar
    private Texture dropBullet;        // utk gambar
    private Texture upBullet;        // utk gambar

    private Texture imgPlayer;    // utk gambar
    private Texture AutoCannon;
    private Sound dropSound;        // utk trigger sound
    private Music rainMusic;        // utk background music

    private OrthographicCamera camera;    // utk camera 2d

    private Rectangle playerShip;
    private Array<Rectangle> bulletDrops;
    private Array<Rectangle> bulletShots;
    private long lastDropTime;
    private long lastUpTime;


    @Override
    public void create() {
        // image asset initialization
        dropBullet = new Texture((Gdx.files.internal("Bullet-down-small.gif")));
        upBullet = new Texture((Gdx.files.internal("Bullet-up-small.gif")));



        imgPlayer = new Texture((Gdx.files.internal("PlayerShip.png")));
        AutoCannon = new Texture((Gdx.files.internal("Auto Cannon.gif")));


        // sound asset initialization
//		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
//		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp4"));

        // start background music
//		rainMusic.setLooping(true);
//		rainMusic.play();

        // create camera and sprite
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 900);
        batch = new SpriteBatch();

        // rectangle player (untuk ambil koordinat nya)
        playerShip = new Rectangle();
        playerShip.x = 30;        // position
        playerShip.y = 30;
        playerShip.width = 45;    // size in pixel
        playerShip.height = 45;


        bulletDrops = new Array<>();
        bulletShots = new Array<>();
        spawnBulletDrop();
    }

    private void spawnBulletDrop() {
        Rectangle bulletDown = new Rectangle();
        // lokasi bulletDown spawn
        bulletDown.x = MathUtils.random(0, 600 - 64);
        bulletDown.y = 900;

        // besar bulletDown
        bulletDown.width = 10;
        bulletDown.height = 20;

        bulletDrops.add(bulletDown);                // utk menambah jumlah di arraynya
        lastDropTime = TimeUtils.nanoTime();    // waktu spawn
    }

    private void spawnBulletShot(float shotX, float shotY) {
        Rectangle bulletShot = new Rectangle();
        // lokasi bulletDown spawn
        bulletShot.x = shotX;
        bulletShot.y = shotY;

        // besar bulletDown
        bulletShot.width = 10;
        bulletShot.height = 10;

        bulletShots.add(bulletShot);                // utk menambah jumlah di arraynya
        lastUpTime = TimeUtils.nanoTime();    // waktu spawn
    }


    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);    // warna background
        ShapeRenderer shapeRenderer;

        // player hitbox check
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(playerShip.x, playerShip.y, playerShip.width, playerShip.height);
        shapeRenderer.end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // tempat spawn asset
        batch.begin();
        batch.draw(AutoCannon, playerShip.x - 13, playerShip.y - 5);
        batch.draw(imgPlayer, playerShip.x - 8, playerShip.y);        // bucket asset

        // obstacle bullet (temp)
        for (Rectangle drop : bulletDrops) {                // bulletDrop asset
            batch.draw(dropBullet, drop.x, drop.y);
        }

        // player shooting (temp)
        for (Rectangle shot : bulletShots) {                // plyer bullet asset
            batch.draw(upBullet, shot.x, shot.y);
        }
        batch.end();

        // movement with mouse
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set((Gdx.input.getX()), Gdx.input.getY(), 0);
            camera.unproject(touchPos);                                //disesuaikan dengan layar game klo ngga bakal over
            playerShip.x = (int) (touchPos.x - 50 / 2);
            playerShip.y = (int) (touchPos.y - 50 / 2);
        }

        // bucket logic (movement berhenti di batas window)
        if (playerShip.x < 0) playerShip.x = 0;
        if (playerShip.x > 600 - 64) playerShip.x = 600 - 64;
        if (playerShip.y < 0) playerShip.y = 0;
        if (playerShip.y > 900 - 64) playerShip.y = 900 - 64;

        // raindrop logic
        if (TimeUtils.nanoTime() - lastDropTime > 500000000) {    // bulletdrops spawn rate control
            spawnBulletDrop();
        }

        if (TimeUtils.nanoTime() - lastUpTime > 150000000) {    // bulletshots spawn rate control
            spawnBulletShot(playerShip.x - 1, playerShip.y + 40);
            spawnBulletShot(playerShip.x + 35, playerShip.y + 40);
        }

        for (Array.ArrayIterator<Rectangle> iter = bulletDrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();        // logic raindrop turun terus

            if (raindrop.y + 20 < 0) iter.remove();                 // remove raindrop di bawah layar

            if (raindrop.overlaps(playerShip)) {                        // check overlap
//				dropSound.play();
                iter.remove();
            }
        }

        for (Array.ArrayIterator<Rectangle> iters = bulletShots.iterator(); iters.hasNext(); ) {
            Rectangle bulletshot = iters.next();
            bulletshot.y += 1000 * Gdx.graphics.getDeltaTime();        // logic bulletshot turun terus

            if (bulletshot.y + 20 > 900) iters.remove();                 // remove bulletshot di atas layar
        }

    }

    @Override
    public void dispose() {        // untuk delete semua (megurangi load di ram)
        dropBullet.dispose();
        upBullet.dispose();
        imgPlayer.dispose();
        AutoCannon.dispose();
        batch.dispose();
    }


}
