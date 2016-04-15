package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.screens.GameScreen;

public class MyGdxGame extends Game {

    private Music gameMusic;



    @Override
    public void create() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/POL-tunnels-short.mp3"));

        // start the playback of the background music immediately
        gameMusic.setLooping(true);
        gameMusic.play();
        setScreen(new GameScreen());
    }
}
