package com.taj.ourmonopoly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class TitleScreen extends ScreenAdapter {

    GameApp game;
    Stage stage;
    Skin skin;
    Label text;
    Image logo;
    TextureAtlas diceAtlas;
    Animation<TextureRegion> dice;

    float timeE = 0;

    public TitleScreen(GameApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.ESCAPE:
                        System.exit(0);
                    default:
                        game.setScreen(new GameScreen(game));
                        return true;
                }
            }
        });
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        text = new Label("Press any key to continue", skin);
        text.setColor(Color.WHITE);
        text.setAlignment(Align.center);
        text.setPosition(
            (GameApp.WINDOW_WIDTH - text.getWidth()) / 2,
            (GameApp.WINDOW_HEIGHT - text.getHeight()) / 2 - 250
        );

        logo = new Image(new Texture("logo.png"));
        logo.setAlign(Align.center);
        logo.setPosition(
            (GameApp.WINDOW_WIDTH - logo.getWidth()) / 2,
            (GameApp.WINDOW_HEIGHT - logo.getImageHeight()) / 2
        );

        diceAtlas = new TextureAtlas(Gdx.files.internal("dice.atlas"));
        dice = new Animation<>(1 / 2f, diceAtlas.getRegions());

        Gdx.input.setInputProcessor(stage);
        stage.addActor(text);
        stage.addActor(logo);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        game.batch.begin();
        game.batch.draw(dice.getKeyFrame(timeE += delta, true), 0, 0);
        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}