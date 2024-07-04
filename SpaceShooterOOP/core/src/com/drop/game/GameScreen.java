package com.drop.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameScreen implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture upBullet, AutoCannon, Background1, Background2;
    private OrthographicCamera camera;
    private Array<Bullet> bulletDrops;
    private Array<Rectangle> bulletShots;
    private Player player;
    private Enemy scout1, scout2, scout3, bomber1, bomber2;
    private Bullet scoutBullet, bomberBullet;
    private long lastDropBullet,lastDropBomb, lastUpTime;
    private int yb1;
    private int yb2 = 3000;
    private BitmapFont font;
    private GlyphLayout layout;
    Sound enemyKilled, playerHit;
    private Music bgm;

    // Object pools
    private Pool<Bullet> bulletPool = Pools.get(Bullet.class);
    private Pool<Rectangle> rectanglePool = Pools.get(Rectangle.class);

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize asset
        upBullet = new Texture(Gdx.files.internal("Bullet-up-small.gif"));
        AutoCannon = new Texture(Gdx.files.internal("Auto Cannon.gif"));

        // Background Asset
        Background1 = new Texture(Gdx.files.internal("Space_Background_7.png"));
        Background2 = new Texture(Gdx.files.internal("Space_Background_7.png"));

        // Sound Sfx Asset
        enemyKilled = Gdx.audio.newSound(Gdx.files.internal("enemyKilled.wav"));
        playerHit = Gdx.audio.newSound(Gdx.files.internal("playerHit.wav"));

        // Music Asset
        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgmSS.mp3"));
        bgm.setLooping(true);
        bgm.play();

        // Initialize player entity
        player = new Player();

        // Initialize enemy entity
        scout1 = new Scout(75);
        scout2 = new Scout(525);
        scout3 = new Scout(300);

        bomber1 = new Bomber(180);
        bomber2 = new Bomber(420);

        scoutBullet = new scoutBullet();
        bomberBullet = new bomberBullet();

        font = new BitmapFont();
        layout = new GlyphLayout();

        // Create camera and sprite batch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 900);
        batch = new SpriteBatch();

        // Initialize arrays
        bulletDrops = new Array<>();
        bulletShots = new Array<>();

        //Ukuran font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MinecraftTen-VGORe.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;  // Ubah ukuran sesuai kebutuhan
        font = generator.generateFont(parameter);
        generator.dispose();

        layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Enemy Track Location Player
        scout1.setRotation(scout1.lockToPlayer(player) - 180);
        scout2.setRotation(scout2.lockToPlayer(player) - 180);
        scout3.setRotation(scout3.lockToPlayer(player) - 180);

        // Spawn asset start
        batch.begin();

        //todo : background looping logic
        batch.draw(Background1, 0, yb1);
        yb1 -= 1;
        batch.draw(Background2, 0, yb2);
        yb2 -= 1;
        if (yb1 == -3000) yb1 = 3000;
        if (yb2 == -3000) yb2 = 3000;

        // score dan hp display
        String scoreText = "Score: " + player.getScore();
        String hpText = "HP: " + player.getHp();
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, 10, 900 - layout.height - 10);  // Posisi untuk score di kiri atas

        layout.setText(font, hpText);
        font.draw(batch, hpText, 10, 900 - layout.height - 50);  // Posisi untuk HP di bawah score

        // Display player ship and auto cannon
        batch.draw(AutoCannon, player.getHitbox().x - 13, player.getHitbox().y - 5);
        batch.draw(player.getImgAsset(), player.getHitbox().x - 8, player.getHitbox().y);

        // Bomber Bullet drop fire rate 2 sec 2e+9
        if (TimeUtils.nanoTime() - lastDropBomb > 2e+9) {
            spawnBulletDrop(bomber1, bomberBullet, 0);
            spawnBulletDrop(bomber2, bomberBullet, 0);
        }

        // Bullet movement
        for (int i = bulletDrops.size - 1; i >= 0; i--) {
            Bullet bullet = bulletDrops.get(i);
            bullet.setBulletX(bullet.getBulletX() + bullet.getBulletVelocityX() * Gdx.graphics.getDeltaTime());
            bullet.setBulletY(bullet.getBulletY() + bullet.getBulletVelocityY() * Gdx.graphics.getDeltaTime());

            int x = (int) bullet.getBulletX();
            int y = (int) bullet.getBulletY();

            bullet.enemyMove(batch, x, y, (int) bullet.getHitbox().getWidth(), (int) bullet.getHitbox().getHeight());

            if (bullet.getyCoord() + 20 < 0) bulletDrops.removeIndex(i);

            if (bullet.getHitbox().overlaps(player.getHitbox())) {
                bulletDrops.removeIndex(i);
                player.setHp(player.getHp() - bullet.getDamage());
                System.out.println("Player hp: " + player.getHp());
                playerHit.play();
            }
        }

        // Enemy bomber logic
        bomber1.enemyMoveShoot(batch);
        bomber2.enemyMoveShoot(batch);

        // Scout Bullet drop fire rate 0.5 sec 500000000
        if (TimeUtils.nanoTime() - lastDropBullet > 500000000) {
            spawnBulletDrop(scout1, scoutBullet, scout1.lockToPlayer(player));
            spawnBulletDrop(scout2, scoutBullet, scout2.lockToPlayer(player));
            spawnBulletDrop(scout3, scoutBullet, scout3.lockToPlayer(player));
        }

        // Enemy scout  logic
        scout1.enemyMoveShoot(batch);
        scout2.enemyMoveShoot(batch);
        scout3.enemyMoveShoot(batch);

        // Player shooting logic
        for (Rectangle shot : bulletShots) {
            batch.draw(upBullet, shot.x, shot.y);

            scout1.enemyHitCheck(player,shot,enemyKilled,batch,bulletShots);
            scout2.enemyHitCheck(player,shot,enemyKilled,batch,bulletShots);
            scout3.enemyHitCheck(player,shot,enemyKilled,batch,bulletShots);
            bomber1.enemyHitCheck(player,shot,enemyKilled,batch,bulletShots);
            bomber2.enemyHitCheck(player,shot,enemyKilled,batch,bulletShots);
        }

        // enemy and player tubruk logic
        scout1.collusionCheck(player,enemyKilled,batch);
        scout2.collusionCheck(player,enemyKilled,batch);
        scout3.collusionCheck(player,enemyKilled,batch);
        bomber1.collusionCheck(player,enemyKilled,batch);
        bomber2.collusionCheck(player,enemyKilled,batch);

        // Player dead?
        if (player.isGameOver()) {
            game.setScreen(new GameOverScreen(game));
            bgm.stop();
            dispose();
            return;
        }

        // Player ship logic
        player.playerMovement(camera);

        // Player move area restriction
        player.playerMoveRestriction();

        if (TimeUtils.nanoTime() - lastUpTime > 150000000) {
            spawnBulletShot(player.getHitbox().x - 1, player.getHitbox().y + 40);
            spawnBulletShot(player.getHitbox().x + 35, player.getHitbox().y + 40);
        }

        //pause jika ditanya
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game));
            bgm.pause();
        }

        for (Array.ArrayIterator<Rectangle> iters = bulletShots.iterator(); iters.hasNext(); ) {
            Rectangle bulletshot = iters.next();
            bulletshot.y += 1000 * Gdx.graphics.getDeltaTime();

            if (bulletshot.y + 20 > 920) iters.remove();
        }

        // Spawn asset end
        batch.end();
    }

    private void spawnBulletDrop(Enemy enemy, Bullet myBullet, int angle) {
        Bullet bullet = bulletPool.obtain();
        bullet.setImgAsset(String.valueOf(myBullet.getImgAsset()));
        bullet.setHitbox(enemy.getMiddleX() - (int) myBullet.getImage().getWidth()/2, enemy.getMiddleY() , (int) myBullet.getImage().getWidth(), (int) myBullet.getImage().getHeight());
        bullet.setImage((int) myBullet.getImage().getWidth(), (int) myBullet.getImage().getHeight());
        bullet.setPosition((int) (enemy.getMiddleX() - myBullet.getImage().getWidth()/2), (int) (enemy.getMiddleY() - myBullet.getImage().getHeight()/2));        bullet.setDamage(myBullet.getDamage());

        bullet.setAngle(angle);
        bullet.setRotation(bullet.getAngle());

        float radian = (float) Math.toRadians(angle - 90);
        bullet.setBulletVelocityX((float) Math.cos(radian) * myBullet.getSpeed());
        bullet.setBulletVelocityY((float) Math.sin(radian) * myBullet.getSpeed());

        bullet.setBulletX(enemy.getMiddleX() - 5);
        bullet.setBulletY(enemy.getMiddleY() - 5);

        bulletDrops.add(bullet);

        // Update lastDropTime for bomber enemies
        if (enemy instanceof Bomber) {
            lastDropBomb = TimeUtils.nanoTime();
        } else if (enemy instanceof Scout) {
            lastDropBullet = TimeUtils.nanoTime();
        }
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
        upBullet.dispose();
        AutoCannon.dispose();
        batch.dispose();
    }
}