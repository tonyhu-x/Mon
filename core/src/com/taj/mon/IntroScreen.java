package com.taj.mon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class IntroScreen extends ScreenAdapter {
    
    private GameApp game;
    private Stage stage;
    private Skin skin;
    private TextField f1;
    private TextField f2;
    private TextField f3;
    private TextField f4;
    private TextButton startButton;

    public IntroScreen(GameApp game) {
        this.game = game;
	}

	@Override
    public void show() {
        stage = new Stage();    
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        f1 = new TextField("Player 1", skin);
        f1.setSize(300, 50);
        f1.setPosition(100, 100);
        f1.setAlignment(Align.center);

        f2 = new TextField("Player 2", skin);
        f2.setSize(300, 50);
        f2.setPosition(100, 200);
        f2.setAlignment(Align.center);

        f3 = new TextField("Player 3", skin);
        f3.setSize(300, 50);
        f3.setPosition(100, 300);
        f3.setAlignment(Align.center);

        f4 = new TextField("Player 4", skin);
        f4.setPosition(100, 400);
        f4.setSize(300, 50);
        f4.setAlignment(Align.center);

        startButton = new TextButton("Start", skin);
        startButton.setSize(300, 100);
        startButton.setPosition(500, 500);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, new String[] {
                    f1.getText(),
                    f2.getText(),
                    f3.getText(),
                    f4.getText()
                }));
            }
        });

        stage.addActor(f1);
        stage.addActor(f2);
        stage.addActor(f3);
        stage.addActor(f4);
        stage.addActor(startButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}