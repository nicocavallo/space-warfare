package com.mygdx.game.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Constants;

import java.util.ArrayList;
import java.util.List;

public abstract class Group {

    protected final Texture img;
    private final List<Item> items;
    private final List<Item> itemsToRemove;
    protected float itemCoolDownSecs = .5f;
    private float itemCoolDownAcc = 0;

    public Group(Texture img) {
        this.img = img;
        this.items = new ArrayList<Item>();
        this.itemsToRemove = new ArrayList<Item>();
    }

    public Group spawn(float x, float y, float speedX, float speedY, float delta) {
        //Fire items
        if (itemCoolDownAcc > itemCoolDownSecs) {
            create(x, y, speedX, speedY);
            itemCoolDownAcc = 0;
        } else {
            itemCoolDownAcc += delta;
        }
        return this;
    }

    protected Group create(float x, float y, float speedX, float speedY) {
        return this.add(new Item(img, x, y, speedX, speedY));
    }

    protected Group add(Item item) {
        this.items.add(item);
        return this;
    }

    public Group update(float delta) {

        for (Item item : this.items) {
            item.update(delta);
        }

        for (Item item : this.items) {
            if (item.getX() > Constants.APP_WIDTH) {
                itemsToRemove.add(item);
            } else if (item.getX() < -item.getWidth()) {
                itemsToRemove.add(item);
            }

            if (item.getY() < 0) {
                itemsToRemove.add(item);
            } else if (item.getY() > Constants.APP_HEIGHT - item.getHeight()) {
                itemsToRemove.add(item);
            }
        }

        items.removeAll(itemsToRemove);
        itemsToRemove.clear();

        return this;
    }

    public Group render(SpriteBatch batch) {
        //

        for (Item item : this.items) {
            item.draw(batch);
        }
        return this;
    }

    public void overlaps(Group group, CollisionHandler collisionHandler) {
        for (Item item1 : this.items) {
            for (Item item2 : group.items) {
                if (item1.overlaps(item2)) {
                    collisionHandler.onCollision(item1, item2);
                }
            }
        }
    }

    public void overlaps(Item otherItem, CollisionHandler collisionHandler) {
        for (Item item1 : this.items) {
            if (item1.overlaps(otherItem)) {
                collisionHandler.onCollision(item1, otherItem);
            }
        }
    }

    public void remove(Item item1) {
        this.itemsToRemove.add(item1);
    }

    public static class Item {

        protected final Sprite sprite;
        protected float speedX = 0;
        protected float speedY = 3;
        private boolean active = true;

        public Item(Texture img, float x, float y, float speedX, float speedY) {
            this.sprite = new Sprite(img);
            sprite.setCenter(x, y + sprite.getHeight() / 2);
            this.speedX = speedX;
            this.speedY = speedY;
        }

        public void update(float delta) {
            if (active)
                this.sprite.setPosition(sprite.getX() + speedX, sprite.getY() + speedY);
        }

        public void draw(SpriteBatch batch) {
            if (active)
                this.sprite.draw(batch);
        }

        public float getX() {
            return sprite.getX();
        }

        public float getWidth() {
            return sprite.getWidth();
        }

        public float getY() {
            return sprite.getY();
        }

        public float getHeight() {
            return sprite.getHeight();
        }

        public boolean overlaps(Item other) {
            return active && other.active &&
                    sprite.getBoundingRectangle()
                            .overlaps(other.sprite.getBoundingRectangle());
        }

        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

        public void deactivate() {
            this.active = false;
        }
    }

}
