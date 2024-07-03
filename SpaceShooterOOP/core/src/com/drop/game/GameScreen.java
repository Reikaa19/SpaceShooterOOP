package com.drop.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.drop.game.Bullet;
import com.drop.game.Enemy;
import com.drop.game.Player;

public class GameScreen implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture dropBullet, upBullet, AutoCannon,Background1,Background2;
    private OrthographicCamera camera;
    private Array<Bullet> bulletDrops;
    private Array<Rectangle> bulletShots;
    private Array<Enemy> enemyDrops;
    private Player player;
    private Enemy scout, scout2;
    private Bullet scoutBullet;
    private long lastDropTime, lastUpTime;
    private int temp;
    private int yb1;
    private int yb2=3000;

    // Object pools
    private Pool<Bullet> bulletPool = Pools.get(Bullet.class);
    private Pool<Rectangle> rectanglePool = Pools.get(Rectangle.class);

    public GameScreen(Game game) {
        this.game = game;
    }

    private int lockToPlayer(Enemy enemy) {
        double test = Math.atan2(enemy.getxCoord() - player.getxCoord() + 2, enemy.getyCoord() - player.getyCoord() + 2);
        int angle = (int) Math.toDegrees(test) * -1;
        return angle;
    }

    private int middleX(Enemy enemy) {
        return (int) enemy.getImage().getX() + enemy.getImage().getRegionWidth() / 2;
    }

    private int middleY(Enemy enemy) {
        return (int) enemy.getImage().getY() + enemy.getImage().getRegionHeight() / 2;
    }

    private void spawnBulletDrop(Enemy enemy) {
        Bullet bullet = bulletPool.obtain();
        bullet.setImgAsset("Bullet-down-small.gif");
        bullet.setHitbox(middleX(enemy) - 5, middleY(enemy) - 5, 10, 10);
        bullet.setImage(10, 16);
        bullet.setPosition(middleX(enemy) - 5, middleY(enemy) - 5);

        int angle = lockToPlayer(enemy);
        bullet.setAngle(angle);
        bullet.setRotation(bullet.getAngle());

        int speed = 300;
        float radian = (float) Math.toRadians(angle - 90);
        bullet.setBulletVelocityX((float) Math.cos(radian) * speed);
        bullet.setBulletVelocityY((float) Math.sin(radian) * speed);

        bullet.setBulletX(middleX(enemy) - 5);
        bullet.setBulletY(middleY(enemy) - 5);

        bulletDrops.add(bullet);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnBulletShot(float shotX, float shotY) {
        Rectangle bulletShot = rectanglePool.obtain();
        bulletShot.x = shotX;
        bulletShot.y = shotY;
        bulletShot.width = 10;
        bulletShot.height = 10;

        bulletShots.add(bulletShot);
        lastUpTime = TimeUtils.nanoTime();
    }

    private void enemyMove(Enemy enemy, int x, int y, int width, int height) {
        enemy.setPosition(x, y);
        enemy.setHitbox(x, y, width, height);
        enemy.draw(batch);
    }

    private void enemyMoveShoot(Enemy enemy) {
        int randomX = MathUtils.random(0, 600 - 48);
        if (enemy.getyCoord() == 0) {
            enemyMove(enemy, randomX, 900, 48, 68);
        } else {
            enemyMove(enemy, enemy.getxCoord(), enemy.getyCoord() - 1, 48, 68);
        }
    }

    private void deadCheck(Enemy enemy){
        int randomX = MathUtils.random(0, 600 - 48);
        if(enemy.getHp() < 0){
            enemyMove(enemy, randomX, 900, 48, 68);
            enemy.setHp(enemy.getMaxHp());
        }
    }

    @Override
    public void show() {
        // Initialize assets
        dropBullet = new Texture(Gdx.files.internal("Bullet-down-small.gif"));
        upBullet = new Texture(Gdx.files.internal("Bullet-up-small.gif"));
        AutoCannon = new Texture(Gdx.files.internal("Auto Cannon.gif"));
        Background1 = new Texture(Gdx.files.internal("Space_Background_7.png"));
        Background2 = new Texture(Gdx.files.internal("Space_Background_7.png"));

        // Initialize player entity
        player = new Player();
        player.setImgAsset("PlayerShip.png");
        player.setHitbox(30, 30, 45, 45);
        player.setHp(20);

        // Initialize enemy entity
        scout = new Enemy("Scout_Engine.gif", 300, 900, 48, 68, 5);
        scout2 = new Enemy("Scout_Engine.gif", 150, 900, 48, 68, 5);


        // Create camera and sprite batch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 900);
        batch = new SpriteBatch();

        // Initialize arrays
        bulletDrops = new Array<>();
        bulletShots = new Array<>();
        enemyDrops = new Array<>();

        // Spawn initial bullet
        spawnBulletDrop(scout);
        spawnBulletDrop(scout2);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        // Update player and enemy positions
        player.hitboxCheck();
        scout.hitboxCheck();
        scout2.hitboxCheck();


        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Player Tracker
        lockToPlayer(scout);
        scout.setRotation(lockToPlayer(scout) - 180);

        lockToPlayer(scout2);
        scout2.setRotation(lockToPlayer(scout2) - 180);

        // Spawn asset start
        batch.begin();

        //show background
        batch.draw(Background1,0,yb1);
        yb1-=2;
        batch.draw(Background2,0,yb2);
        yb2-=2;
        if (yb1 == -3000) yb1 = 3000;
        if (yb2 == -3000) yb2 = 3000;

        // Render player ship and basic gun
        batch.draw(AutoCannon, player.getHitbox().x - 13, player.getHitbox().y - 5);
        batch.draw(player.getImgAsset(), player.getHitbox().x - 8, player.getHitbox().y);

        // Bullet drop fire rate logic
        if (TimeUtils.nanoTime() - lastDropTime > 500000000) {
            spawnBulletDrop(scout);
            spawnBulletDrop(scout2);
        }

        if (player.getHp() <= 0) {
            System.out.println("Game Over");
            game.setScreen(new GameOverScreen(game));
            dispose();
            return;
        }

        // Bullet movement
        for (int i = bulletDrops.size - 1; i >= 0; i--) {
            Bullet bullet = bulletDrops.get(i);
            bullet.setBulletX(bullet.getBulletX() + bullet.getBulletVelocityX() * Gdx.graphics.getDeltaTime());
            bullet.setBulletY(bullet.getBulletY() + bullet.getBulletVelocityY() * Gdx.graphics.getDeltaTime());

            int x = (int) bullet.getBulletX();
            int y = (int) bullet.getBulletY();

            enemyMove(bullet, x, y, 10, 15);

            if (bullet.getyCoord() + 20 < 0) bulletDrops.removeIndex(i);

            if (bullet.getHitbox().overlaps(player.getHitbox())) {
                bulletDrops.removeIndex(i);
                player.setHp(player.getHp() - 1);
                System.out.println("Player hp: " + player.getHp());
            }
        }

        // Enemy movement
        enemyMoveShoot(scout);
        enemyMoveShoot(scout2);

        // Player shooting logic
        for (Rectangle shot : bulletShots) {
            batch.draw(upBullet, shot.x, shot.y);

            if (scout.getHitbox().overlaps(shot)) {
                scout.setHp(scout.getHp() - 1);
                if (scout.getHp() == 0) {
                    player.setScore(player.getScore() + 1);
                    System.out.println("Score: " + player.getScore());
                }
                deadCheck(scout);


                bulletShots.removeValue(shot, true); // Remove the bullet shot
            }
            if (scout2.getHitbox().overlaps(shot)) {
                scout2.setHp(scout2.getHp() - 1);
                if (scout2.getHp() == 0) {
                    player.setScore(player.getScore() + 1);
                    System.out.println("Score: " + player.getScore());
                }
                deadCheck(scout2);
                bulletShots.removeValue(shot, true); // Remove the bullet shot
            }
        }

        // enemy and player tubruk logic
        if (player.getHitbox().overlaps(scout.getHitbox())) {
            player.setHp(player.getHp() - scout.getHp());
            System.out.println("Player hp: " + player.getHp());
            scout.setHp(-1);
            deadCheck(scout);
        }

        if (player.getHitbox().overlaps(scout2.getHitbox())) {
            player.setHp(player.getHp() - scout2.getHp());
            System.out.println("Player hp: " + player.getHp());
            scout2.setHp(-1);
            deadCheck(scout2);
        }

        if (player.getHp() == 0) {
            System.out.println("Game Over");
        }

        // Spawn asset end
        batch.end();

        // Player ship logic
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set((Gdx.input.getX()), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            player.getHitbox().x = (int) (touchPos.x - 50 / 2);
            player.getHitbox().y = (int) (touchPos.y - 50 / 2);
        }

        // Player move area restriction
        if (player.getHitbox().x < 10) player.getHitbox().x = 10;
        if (player.getHitbox().x > 600 - 54) player.getHitbox().x = 600 - 54;
        if (player.getHitbox().y < 10) player.getHitbox().y = 10;
        if (player.getHitbox().y > 900 - 64) player.getHitbox().y = 900 - 64;

        if (TimeUtils.nanoTime() - lastUpTime > 150000000) {
            spawnBulletShot(player.getHitbox().x - 1, player.getHitbox().y + 40);
            spawnBulletShot(player.getHitbox().x + 35, player.getHitbox().y + 40);
        }

        //pause jika ditanya
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            game.setScreen(new PauseScreen(game));
//        }
        for (Array.ArrayIterator<Rectangle> iters = bulletShots.iterator(); iters.hasNext(); ) {
            Rectangle bulletshot = iters.next();
            bulletshot.y += 1000 * Gdx.graphics.getDeltaTime();

            if (bulletshot.y + 20 > 920) iters.remove();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        dropBullet.dispose();
        upBullet.dispose();
        AutoCannon.dispose();
        batch.dispose();
    }
}