package com.mygdx.game.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public final class InfiniteScrollBackground extends Actor {
    private final Texture b1;

    public InfiniteScrollBackground(Texture texture) {
        this(texture,10f);
    }

    public InfiniteScrollBackground(Texture texture, float duration) {
        b1 = texture;
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setPosition(0,0);
        addAction(forever(sequence(moveTo(0, -texture.getHeight(),duration), moveTo(0, 0))));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(b1,getX(),getY(),getWidth(),getHeight() * 2);
    }
}