package com.mygdx.game.common;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Constants;
import com.mygdx.game.screens.GameScreen;

public class HUD {

    private final GameScreen game;
    private final Player player;
    private BitmapFont scoreText;
    private BitmapFont lifeText;

    public HUD(GameScreen game, Player player) {
        this.game = game;
        this.scoreText = new BitmapFont();
        this.lifeText = new BitmapFont();
        this.player = player;
    }

    public void render(SpriteBatch batch) {
        scoreText.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        String scoreStr = "Score: " + game.getScore().toString();
        scoreText.draw(batch, scoreStr, Constants.APP_WIDTH - scoreStr.length()*8, Constants.APP_HEIGHT - 20);
        lifeText.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        lifeText.draw(batch, "Health: " + player.getHealth(), 10, Constants.APP_HEIGHT - 20);
    }

    public void update(float delta) {
    }
}
