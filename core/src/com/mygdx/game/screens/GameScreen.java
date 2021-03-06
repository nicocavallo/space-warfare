package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.common.InfiniteScrollBackground;
import com.mygdx.game.common.Bullets;
import com.mygdx.game.common.CollisionHandler;
import com.mygdx.game.common.Enemies;
import com.mygdx.game.common.Group;
import com.mygdx.game.common.HUD;
import com.mygdx.game.common.Player;
import com.mygdx.game.common.Effects;
import com.mygdx.game.common.PlayerSettings;

public class GameScreen extends ScreenAdapter {


    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Player player;
    private final Bullets bullets;
    private final Enemies enemies;
    private final Bullets enemyBullets;
    private final HUD hud;
    private final Effects effects;
    private final Viewport viewPort;
    private final Sound explosion;
    private final Sound playerExplosion;
    private final InfiniteScrollBackground background;
    private final MyGdxGame game;

    public GameScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewPort = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);
        bullets = new Bullets(new Texture("bullet5.png"), Gdx.audio.newSound(Gdx.files.internal("sounds/laser.mp3")));
        player = new Player(this.bullets, game.getSettings());
        player.centerX();
        this.enemyBullets = new Bullets(new Texture("bullet_enemy.png"), Gdx.audio.newSound(Gdx.files.internal("sounds/enemyLaser.mp3")));
        this.enemies = new Enemies(this, enemyBullets);
        this.hud = new HUD(this, player);
        this.effects = new Effects();
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));
        this.playerExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/playerExplosion.mp3"));
        this.background = new InfiniteScrollBackground(new Texture("back_2_vo.png"));
    }

    public Integer getScore() {
        return game.getSettings().getPoints();
    }

    private class EnemyBulletHandler implements CollisionHandler {
        @Override
        public void onCollision(Group.Item item1, Group.Item item2) {
            enemies.remove(item1);
            bullets.remove(item2);
            game.getSettings().incPoints(10);
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
            game.getSettings().incPoints(10);
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
        background.act(delta);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.draw(batch,0);
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
        if (player.isAlive()) {
            if (Gdx.input.isTouched()) {
                Vector3 newPosition = camera
                        .unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                player.moveTo(newPosition.x, 32);
            }
        } else if (Gdx.input.justTouched()) {
            this.game.getSettings().upgradeAll();
            System.out.println(this.game.getSettings().getPoints());
            game.setScreen(new MainMenuScreen(game));
        }

    }
}
