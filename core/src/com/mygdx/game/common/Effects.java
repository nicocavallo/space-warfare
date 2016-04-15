package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.utils.GameUtils.RANDOM;

public class Effects {

    private final List<Effect> effects = new ArrayList<Effect>();
    private final List<Effect> toRemove = new ArrayList<Effect>();
    private final Texture blinkTexture;
    private final Texture explosion = new Texture("explosion.png");

    public Effects() {
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blinkTexture = new Texture(pixmap);
        pixmap.dispose();

    }

    public void rumble(OrthographicCamera camera, float power, float time) {
        this.effects.add(new Rumble(camera, power, time));
    }

    public void blink(OrthographicCamera camera, float time, Color color) {
        this.effects.add(new Blink(camera, time, color));
    }

    public void explosion(OrthographicCamera camera, float time, float x, float y) {
        this.effects.add(new Blink(camera, time, explosion, x, y, RANDOM.nextInt(360)));
    }

    public void update(float delta) {
        for (Effect effect : this.effects) {
            effect.update(delta);
        }
        for (Effect effect : this.toRemove) {
            effects.remove(effect);
        }
        toRemove.clear();
    }

    public void render(SpriteBatch batch) {
        for (Effect effect : this.effects) {
            effect.render(batch);
        }
    }

    public abstract class Effect {

        protected float time;
        protected float currentTime;
        protected OrthographicCamera camera;

        public Effect(float time, OrthographicCamera camera) {
            this.time = time;
            this.camera = camera;
        }

        public final void update(float delta) {
            if (currentTime <= time) {
                doUpdate(delta);
                currentTime += delta;
            } else {
                afterApplied(delta);
                toRemove.add(this);
            }
        }

        protected abstract void afterApplied(float delta);

        protected abstract void doUpdate(float delta);

        public abstract void render(SpriteBatch batch);
    }

    private class Blink extends Effect {
        private Sprite sprite;

        private Blink(OrthographicCamera camera, float time, Color color) {
            super(time, camera);
            sprite = new Sprite(blinkTexture);
            sprite.setColor(color);
            sprite.setAlpha(1f);
        }

        private Blink(OrthographicCamera camera, float time, Texture texture) {
            super(time, camera);
            sprite = new Sprite(texture);
            sprite.setAlpha(1f);
        }

        private Blink(OrthographicCamera camera, float time, Texture texture, float x, float y, int angle) {
            super(time, camera);
            sprite = new Sprite(texture);
            sprite.setCenter(x,y);
            sprite.rotate(angle);
            sprite.setAlpha(1f);
        }


        @Override
        protected void afterApplied(float delta) {
            sprite.setAlpha(0f);
        }

        @Override
        protected void doUpdate(float delta) {
            sprite.setAlpha((time - currentTime)/time);
        }

        @Override
        public void render(SpriteBatch batch) {
            sprite.draw(batch);
        }
    }

    private class Rumble extends Effect {
        private float x, y;
        private float power;
        private float currentPower;

        private Rumble(OrthographicCamera camera, float power, float time) {
            super(time, camera);
            this.power = power;
        }

        @Override
        protected void afterApplied(float delta) {
            // When the shaking is over move the camera back to the hero position
            camera.position.x = Constants.APP_WIDTH / 2;
            camera.position.y = Constants.APP_HEIGHT / 2;
        }

        @Override
        protected void doUpdate(float delta) {
            currentPower = power * ((time - currentTime) / time);
            // generate RANDOM new x and y values taking into account
            // how much force was passed in
            x = (RANDOM.nextFloat() - 0.5f) * 2 * currentPower;
            y = (RANDOM.nextFloat() - 0.5f) * 2 * currentPower;
            // Set the camera to this new x/y position
            camera.translate(-x, -y);
        }

        @Override
        public void render(SpriteBatch batch) {
            //Does nothing
        }
    }

}
