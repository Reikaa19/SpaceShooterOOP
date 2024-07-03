package com.drop.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;


//import java.awt.*;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;        // utk display gambar
    private Texture dropBullet, upBullet, AutoCannon, scoutEngine, scoutShoot;    // utk gambar
    private Sprite enemyScout;

    // Sound asset
    private Sound dropSound;        // utk trigger sound
    private Music rainMusic;        // utk background music

    private OrthographicCamera camera;    // utk camera 2d

    private Array<Rectangle>  bulletShots, enemyList;
    private long lastDropTime, lastUpTime;

    // todo: test code (sementara agar bisa di kenal semua function di MyGdxGame)
    private Player player;
    private Enemy scout;
    private Bullet scoutBullet;

    private Array<Bullet> bulletDrops;
    private int temp;


    @Override
    public void create() {
        // image asset initialization
        dropBullet = new Texture((Gdx.files.internal("Bullet-down-small.gif")));
        upBullet = new Texture((Gdx.files.internal("Bullet-up-small.gif")));
        AutoCannon = new Texture((Gdx.files.internal("Auto Cannon.gif")));

        // initialization player entity
        player = new Player();
        player.setImgAsset("PlayerShip.png");               // player ship image test
        player.setHitbox(30, 30, 45, 45);


        // todo: initialization enemy sentara buat testing
        scout = new Enemy();
        scout.setImgAsset("Scout_Engine.gif");
        scout.setHitbox(300, 500, 48, 68);
        scout.setImage(48, 68);
        scout.setPosition(300, 500);

        // new hitbox test
//        todo: sprite test
        scoutEngine = new Texture((Gdx.files.internal("Scout_Engine.gif")));
        enemyScout = new Sprite(scoutEngine, 0, 0, 48, 68);
        enemyScout.setPosition(300, 500);

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

        bulletDrops = new Array<>();
        bulletShots = new Array<>();
        spawnBulletDrop(scout);
    }

    private int lockToPlayer(Enemy enemy) {
        double test = Math.atan2(enemy.getxCoord() - player.getxCoord() + 2, enemy.getyCoord() - player.getyCoord() + 2);
        int angle = (int) Math.toDegrees(test) * -1;
        return angle;
    }

    private int middleX(Enemy enemy){
        return  (int)enemy.getImage().getX() + enemy.getImage().getRegionWidth()/2;
    }

    private int middleY(Enemy enemy){
        return  (int)enemy.getImage().getY() + enemy.getImage().getRegionHeight()/2;
    }

    private void spawnBulletDrop(Enemy enemy) {
        scoutBullet = new Bullet();
        scoutBullet.setImgAsset("Bullet-down-small.gif");
        scoutBullet.setHitbox(middleX(enemy)-5,middleY(enemy)-5,10,10);
        scoutBullet.setImage(10,16);
        scoutBullet.setPosition(middleX(enemy)-5,middleY(enemy)-5);

        int angle = lockToPlayer(scoutBullet);
        scoutBullet.setAngle(angle);
        scoutBullet.setRotation(scoutBullet.getAngle());

        int speed = 100;
        float radian = (float) Math.toRadians(angle-90);
        scoutBullet.setBulletVelocityX((float) Math.cos(radian) * speed);
        scoutBullet.setBulletVelocityY((float) Math.sin(radian) * speed);

        scoutBullet.setBulletX(middleX(enemy)-5);
        scoutBullet.setBulletY(middleY(enemy)-5);


        bulletDrops.add(scoutBullet);               // utk menambah jumlah di arraynya
        lastDropTime = TimeUtils.nanoTime();        // waktu spawn
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

    private void enemyMove(Enemy enemy, int x, int y, int width, int height) {
        enemy.setPosition(x, y);
        enemy.setHitbox(x, y, width, height);
        enemy.draw(batch);
    }


    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);    // warna background

        // todo: display player hitbox test code
        player.hitboxCheck();
        scout.hitboxCheck();
        scoutBullet.hitboxCheck();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Player Tracker
        lockToPlayer(scout);
        scout.setRotation(lockToPlayer(scout)-180);

        // Spawn asset start
        batch.begin();

        // todo: enemy scout move logic temp
//        temp+=1;
        enemyMove(scout, 150, 500, 48, 68);

        // Render player ship and basic gun
        batch.draw(AutoCannon, player.getHitbox().x - 13, player.getHitbox().y - 5);
        batch.draw(player.getImgAsset(), player.getHitbox().x - 8, player.getHitbox().y);


        // bullet drop fire rate logic
        if (TimeUtils.nanoTime() - lastDropTime > 500000000) {    // bulletdrops spawn rate control
            spawnBulletDrop(scout);
        }


        //todo: bullet movement (done)
        scoutBullet.draw(batch);
        for (int i = bulletDrops.size - 1; i >= 0; i--)  {
            bulletDrops.get(i).setBulletX(bulletDrops.get(i).getBulletX() + bulletDrops.get(i).getBulletVelocityX() * Gdx.graphics.getDeltaTime());
            bulletDrops.get(i).setBulletY(bulletDrops.get(i).getBulletY() + bulletDrops.get(i).getBulletVelocityY() * Gdx.graphics.getDeltaTime());

            int x = (int)bulletDrops.get(i).getBulletX();
            int y = (int)bulletDrops.get(i).getBulletY();

            enemyMove(bulletDrops.get(i),x,y,10,15);

            if (bulletDrops.get(i).getyCoord() + 20 < 0) bulletDrops.removeIndex(i);                           // remove raindrop di bawah layar

            // todo: test code karena hilang nya bullet drop tergantung player hitbox
            if (bulletDrops.get(i).getHitbox().overlaps(player.getHitbox())) {
//				dropSound.play();
                bulletDrops.removeIndex(i);                           // remove raindrop di bawah layar
            }

        }

        // player shooting (temp)
        for (Rectangle shot : bulletShots) {                // plyer bullet asset
            batch.draw(upBullet, shot.x, shot.y);
        }

        // Spawn asset end
        batch.end();

//-----------------------------------------------------------------------------------------
        // player ship logic

        // movement with mouse
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set((Gdx.input.getX()), Gdx.input.getY(), 0);
            camera.unproject(touchPos);                                //disesuaikan dengan layar game klo ngga bakal over

            // player movement logic
            player.getHitbox().x = (int) (touchPos.x - 50 / 2);
            player.getHitbox().y = (int) (touchPos.y - 50 / 2);

        }

        // player move area restriction (done)
        if (player.getHitbox().x < 10) player.getHitbox().x = 10;
        if (player.getHitbox().x > 600 - 54) player.getHitbox().x = 600 - 54;
        if (player.getHitbox().y < 10) player.getHitbox().y = 10;
        if (player.getHitbox().y > 900 - 64) player.getHitbox().y = 900 - 64;

        if (TimeUtils.nanoTime() - lastUpTime > 150000000) {    // bulletshots spawn rate control
            // todo: test code karena spawn bullet tergantung posisi player
            spawnBulletShot(player.getHitbox().x - 1, player.getHitbox().y + 40);
            spawnBulletShot(player.getHitbox().x + 35, player.getHitbox().y + 40);
        }

        for (Array.ArrayIterator<Rectangle> iters = bulletShots.iterator(); iters.hasNext(); ) {
            Rectangle bulletshot = iters.next();
            bulletshot.y += 1000 * Gdx.graphics.getDeltaTime();        // logic bulletshot turun terus

            if (bulletshot.y + 20 > 920) iters.remove();                 // remove bulletshot di atas layar
        }

    }

    @Override
    public void dispose() {        // untuk delete semua (megurangi load di ram)
        dropBullet.dispose();
        upBullet.dispose();
        scoutEngine.dispose();
        AutoCannon.dispose();
        batch.dispose();

        super.dispose();
    }


}
