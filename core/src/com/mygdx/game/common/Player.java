package com.mygdx.game.common;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Constants;

public class Player extends Group.Item {

    private final Bullets bullets;
    private int health = 4;

    public Player(Bullets bullets) {
        super(new Texture("ship.png"),0,0,2,2.5f);
        this.bullets = bullets;
        this.bullets.setShooter(this);
    }

    public float getX() {
        return this.sprite.getX();
    }

    public float getWidth() {
        return this.sprite.getWidth();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getHeight() {
        return this.sprite.getHeight();
    }

    public void update(float delta) {
        if (isAlive()) {
            if (sprite.getX() > Constants.APP_WIDTH - sprite.getWidth()) {
                sprite.setPosition(Constants.APP_WIDTH - sprite.getWidth(), sprite.getY());
            } else if (sprite.getX() < 0) {
                sprite.setPosition(0, sprite.getY());
            }

            if (sprite.getY() < 0) {
                sprite.setPosition(sprite.getX(), 0);
            } else if (sprite.getY() > Constants.APP_HEIGHT - sprite.getHeight()) {
                sprite.setPosition(sprite.getX(), Constants.APP_HEIGHT - sprite.getHeight());
            }
            bullets.spawn(this.getX(), this.getY() + this.getHeight() / 2, 0, 3, delta);
        }
    }

    public void centerX() {
        this.sprite.setPosition(Constants.APP_WIDTH / 2 - sprite.getWidth() / 2, sprite.getY());
    }

    public void hurt() {
        this.health--;
        if (health <= 0)
            super.deactivate();
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void moveTo(float inputX, float inputY) {
        float nextPositionX = inputX - sprite.getWidth()/2;
        float nextPositionY = inputY;

        if (sprite.getX() + sprite.getWidth()/2 + speedX < inputX) {
            nextPositionX = sprite.getX() + speedX;
        } else if (sprite.getX() + sprite.getWidth()/2 - speedX > inputX) {
            nextPositionX = sprite.getX() - speedX;
        }
        if (sprite.getY() + speedY < inputY) {
            nextPositionY = sprite.getY() + speedY;
        } else if (sprite.getY() - speedY > inputY) {
            nextPositionY = sprite.getY() - speedY;
        }
        sprite.setPosition(nextPositionX,nextPositionY);
    }

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return !isAlive();
    }
}
