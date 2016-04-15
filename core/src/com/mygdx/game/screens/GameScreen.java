package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Constants;
import com.mygdx.game.common.Bullets;
import com.mygdx.game.common.CollisionHandler;
import com.mygdx.game.common.Enemies;
import com.mygdx.game.common.Group;
import com.mygdx.game.common.HUD;
import com.mygdx.game.common.Player;
import com.mygdx.game.common.Effects;

public class GameScreen extends ScreenAdapter {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final Player player;
    private final Bullets bullets;
    private final Enemies enemies;
    private final Bullets enemyBullets;
    private Integer score;
    private final HUD hud;
    private Effects effects;
    private final Viewport viewPort;
    private final Sound explosion;
    private final Sound playerExplosion;
    private final Texture background;
    private int backgroundY = 0;

    public GameScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewPort = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);
        bullets = new Bullets(new Texture("bullet.png"));
        player = new Player(this.bullets);
        player.centerX();
        this.enemyBullets = new Bullets(new Texture("bullet_enemy.png"));
        this.enemies = new Enemies(this, enemyBullets);
        this.score = 0;
        this.hud = new HUD(this, player);
        this.effects = new Effects();
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));
        this.playerExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/playerExplosion.mp3"));
        this.background = new Texture("back_2.png");
    }

    public Integer getScore() {
        return score;
    }

    private class EnemyBulletHandler implements CollisionHandler {
        @Override
        public void onCollision(Group.Item item1, Group.Item item2) {
            enemies.remove(item1);
            bullets.remove(item2);
            score += 10;
            effects.rumble(camera, 1f, .5f);
            effects.explosion(camera, .25f, item1.getX() + item1.getWidth() / 2, item1.getY() + item1.getHeight() / 2);
            explosion.play();
        }
    }

    private class EnemyPlayerHandler implements CollisionHandler {
        @Override
        public void onCollision(Group.Item item1, Group.Item item2) {
            enemies.remove(item1);
            effects.explosion(camera, .25f, item1.getX() + item1.getWidth() / 2, item1.getY() + item1.getHeight() / 2);
            player.hurt();
            score += 10;
            effects.rumble(camera, 1f, 1f);
            effects.blink(camera, 0.2f, Color.RED);
            Gdx.input.vibrate(300);
            explosion.play();
            if (player.isDead()) {
                playerExplosion.play();
            }
        }
    }

    private class EnemyBulletPlayerHandler implements CollisionHandler {
        @Override
        public void onCollision(Group.Item item1, Group.Item item2) {
            enemyBullets.remove(item1);
            player.hurt();
            effects.rumble(camera, .2f, .1f);
            effects.blink(camera, 0.2f, Color.RED);
            Gdx.input.vibrate(200);
            if (player.isDead()) {
                playerExplosion.play();
            }
        }
    }

    @Override
    public void render(float delta) {

        handleInput();

        checkCollisions();

        update(delta);

        draw();
    }

    private void checkCollisions() {
        checkCollisions(enemies, bullets, new EnemyBulletHandler());
        checkCollisions(enemies, player, new EnemyPlayerHandler());
        checkCollisions(enemyBullets, player, new EnemyBulletPlayerHandler());
        checkCollisions(enemyBullets, bullets, new CollisionHandler() {
            @Override
            public void onCollision(Group.Item item1, Group.Item item2) {
                enemyBullets.remove(item1);
                bullets.remove(item2);
            }
        });
    }

    private void update(float delta) {
        player.update(delta);
        enemies.update(delta);
        bullets.update(delta);
        enemyBullets.update(delta);
        hud.update(delta);
        effects.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
    }

    private void draw() {
        //r 64
        //g 0
        //b 112
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background,0,backgroundY--);
        player.draw(batch);
        enemies.render(batch);
        bullets.render(batch);
        enemyBullets.render(batch);
        effects.render(batch);
        hud.render(batch);
        batch.end();
    }

    private void checkCollisions(final Group enemies, final Group bullets, CollisionHandler handler) {
        enemies.overlaps(bullets, handler);
    }

    private void checkCollisions(final Group enemies, final Group.Item item, CollisionHandler handler) {
        enemies.overlaps(item, handler);
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 newPosition = camera
                    .unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            player.moveTo(newPosition.x, newPosition.y);
        }
    }
}
