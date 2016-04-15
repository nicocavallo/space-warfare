package com.mygdx.game.common;

/**
 * Created by Nicolas on 28/02/2016.
 */
public interface Collidable {
    void overlaps(Collidable group, CollisionHandler collisionHandler);
}
