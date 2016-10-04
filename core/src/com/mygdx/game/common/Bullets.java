package com.mygdx.game.common;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Bullets extends Group {

    private Item shooter;
    private Sound laser;

    public Bullets(Texture img, Sound laser) {
        super(img);
        this.laser = laser;
    }

    public void setItemCoolDownSecs(float secs) {
        super.itemCoolDownSecs = secs;
    }

    @Override
    protected Group create(float x, float y, float speedX, float speedY) {
        this.laser.play(.25f);
        super.create(x, y, speedX, speedY);
        return super.create(x + shooter.getWidth(), y, speedX, speedY);
    }

    public void setShooter(Item shooter) {
        this.shooter = shooter;
    }

}
