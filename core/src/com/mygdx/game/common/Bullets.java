package com.mygdx.game.common;

import com.badlogic.gdx.graphics.Texture;

public class Bullets extends Group {

    private Item shooter;

    public Bullets(Texture img) {
        super(img);
    }

    public void setItemCoolDownSecs(int secs) {
        super.itemCoolDownSecs = secs;
    }

    @Override
    protected Group create(float x, float y, float speedX, float speedY) {
        super.create(x, y, speedX, speedY);
        return super.create(x + shooter.getWidth(), y, speedX, speedY);
    }

    public void setShooter(Item shooter) {
        this.shooter = shooter;
    }

}
