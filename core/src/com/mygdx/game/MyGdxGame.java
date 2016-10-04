package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.common.PlayerSettings;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;

public class MyGdxGame extends Game {

    private Music gameMusic;
    private PlayerSettings settings;

    @Override
    public void create() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/POL-tunnels-short.mp3"));

        // start the playback of the background music immediately
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        this.settings = new PlayerSettings();
        setScreen(new MainMenuScreen(this));
    }

    public PlayerSettings getSettings() {
        return settings;
    }
}
