package com.mygdx.game.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Constants;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtils;

public class Enemies extends Group {

    private final GameScreen game;
    private final Bullets bullets;

    public Enemies(GameScreen gameScreen, Bullets bullets) {
        super(new Texture("enemy_1.png"));
        this.game = gameScreen;
        this.itemCoolDownSecs = 2;
        this.bullets = bullets;
        this.bullets.setItemCoolDownSecs(2);
    }

    @Override
    public Group update(float delta) {
        if (game.getScore() > 400) {
            this.itemCoolDownSecs = .20f;
        } else if (game.getScore() > 300) {
            this.itemCoolDownSecs = .25f;
        } else if (game.getScore() > 200) {
            this.itemCoolDownSecs = .5f;
        } else if (game.getScore() > 100) {
            this.itemCoolDownSecs = .75f;
        } else if (game.getScore() > 50) {
            this.itemCoolDownSecs = 1.5f;
        }
        this.spawn(GameUtils.RANDOM.nextFloat() * Constants.APP_WIDTH,
                Constants.APP_HEIGHT - 40, 0, -3, delta);
        return super.update(delta);
    }

    @Override
    protected Group create(float x, float y, float speedX, float speedY) {
        Item item = GameUtils.choose(new Item(img, x, y, speedX, speedY));
        if (game.getScore() > 200) {
            item = GameUtils.choose(new Item(img, x, y, speedX, speedY),
                    new Enemy2(x,y,-2,-2),
                    new Enemy2(x,y,2,-2),
                    new Enemy2(x,y,-2,-2),
                    new Enemy2(x,y,2,-2));
        } else if (game.getScore() > 150) {
            item = GameUtils.choose(new Item(img, x, y, speedX, speedY),
                    new Enemy2(x,y,-2,-2),
                    new Enemy2(x,y,2,-2));
        } else if (game.getScore() > 50) {
            item = GameUtils.choose(new Item(img, x, y, speedX, speedY),
                    new Item(img, x, y, speedX, speedY),
                    new Enemy2(x,y,2,-2));
        }
        return super.add(item);
    }

    public Bullets getBullets() {
        return bullets;
    }

    private class Enemy2 extends Item {
        public Enemy2(float x, float y, float speedX, float speedY) {
            super(new Texture("enemy_2.png"), x, y, speedX, speedY);
        }

        @Override
        public void update(float delta) {
            if (this.getX() <= 0) {
                this.setSpeedX(GameUtils.abs(speedX));
            } else if (this.getX() >= Constants.APP_WIDTH - this.getWidth()) {
                this.setSpeedX(-GameUtils.abs(speedX));
            }
            super.update(delta);
            bullets.setShooter(this);
            bullets.spawn(this.getX(), this.getY(), 0, -3, delta);
        }

        @Override
        public void draw(SpriteBatch batch) {
            super.draw(batch);
        }
    }
}
