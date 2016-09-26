package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Constants;
import com.mygdx.game.common.InfiniteScrollBackground;

public class MainMenuScreen implements Screen {

    private final Game game;

    private Stage stage;
    private Viewport viewPort;
    private OrthographicCamera camera;

    private TextButton playButton;
    private TextButton optionsButton;
    private TextButton creditsButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private VerticalGroup vGroup;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewPort = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);
        stage.setViewport(viewPort);
        Gdx.input.setInputProcessor(stage);
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont(Gdx.files.internal("fonts/8-bit32.fnt"));
        textButtonStyle.font = font;
        playButton = new TextButton("Play", textButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
                game.setScreen(new GameScreen(game));
            }
        });
        optionsButton = new TextButton("Options", textButtonStyle);

        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont(Gdx.files.internal("fonts/8-bit.fnt"));
        textButtonStyle.font = font;
        creditsButton = new TextButton("Credits", textButtonStyle);

        vGroup = new VerticalGroup();
        vGroup.addActor(playButton);
        vGroup.addActor(optionsButton);
        vGroup.addActor(creditsButton);
        vGroup.center();
        vGroup.space(32);

        vGroup.setX(stage.getWidth()/2 - vGroup.getWidth()/2);
        System.out.println(vGroup.getHeight()/2);
        vGroup.setY((stage.getHeight()/3)*2);

        stage.addActor(new InfiniteScrollBackground(new Texture(Gdx.files.internal("back_2.png"))));

        stage.addActor(vGroup);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
