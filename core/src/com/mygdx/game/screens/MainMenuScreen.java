package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.common.InfiniteScrollBackground;

public class MainMenuScreen implements Screen {

    private final MyGdxGame game;

    private Stage stage;
    private Viewport viewPort;
    private OrthographicCamera camera;

    private TextButton playButton;
    private TextButton optionsButton;
    private TextButton creditsButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private VerticalGroup menuGroup;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewPort = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);
        stage = new Stage();
        stage.setViewport(viewPort);
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont(Gdx.files.internal("fonts/8-bit32.fnt"));
        textButtonStyle.font = font;

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;

        playButton = new TextButton("Play", textButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
                game.setScreen(new GameScreen(game));
            }
        });
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont(Gdx.files.internal("fonts/8-bit.fnt"));
        textButtonStyle.font = font;
        optionsButton = new TextButton("Options", textButtonStyle);
        creditsButton = new TextButton("Credits", textButtonStyle);

        menuGroup = new VerticalGroup();
        menuGroup.addActor(playButton);
        menuGroup.addActor(optionsButton);
        menuGroup.addActor(creditsButton);
        menuGroup.center();
        menuGroup.space(24);

        menuGroup.setX(stage.getWidth()/2 - menuGroup.getWidth()/2);
        System.out.println(menuGroup.getHeight()/2);
        menuGroup.setY((stage.getHeight()/3)*2);

        stage.addActor(new InfiniteScrollBackground(new Texture(Gdx.files.internal("back_2_vo.png"))));

        stage.addActor(menuGroup);

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
