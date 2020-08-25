package com.taj.mon;

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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;


public class TitleScreen extends ScreenAdapter {

    private class SettingsDialog extends Dialog {

        public SettingsDialog() {
            super("Settings", skin);
            var table = this.getContentTable();
            Label resLabel = new Label("Resolution", skin);
            TextButton resLow = new TextButton(GameApp.WIN_WIDTH_LOW + " * " + GameApp.WIN_HEIGHT_LOW, skin);
            resLow.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.graphics.setWindowedMode(1280, 720);
                    stage.getViewport().update(1280, 720);
                }
            });

            TextButton resHigh = new TextButton(GameApp.WIN_WIDTH_HIGH + " * " + GameApp.WIN_HEIGHT_HIGH, skin);
            resHigh.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.graphics.setWindowedMode(1920, 1080);
                    stage.getViewport().update(1920, 1080);
                }
            });
            table.add(resLabel);
            table.add(resLow);
            table.add(resHigh);
        }
    }

    GameApp game;
    Stage stage;
    Skin skin;
    Label text;
    Image logo;
    TextButton settingsButton;
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
                    case Input.Keys.S:
                        settings();
                        break;
                    case Input.Keys.ESCAPE:
                        System.exit(0);
                        break;
                    default:
                        game.setScreen(new IntroScreen(game));
                        break;
                    }
                    return true;
            }
        });
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        int wid = Gdx.graphics.getWidth();
        int hei = Gdx.graphics.getHeight();

        text = new Label("Press any key to continue", skin);
        text.setColor(Color.WHITE);
        text.setAlignment(Align.center);
        text.setPosition(
            (wid - text.getWidth()) / 2,
            (hei - text.getHeight()) / 2 - 250
        );

        logo = new Image(new Texture("logo.png"));
        logo.setAlign(Align.center);
        logo.setPosition(
            (wid - logo.getWidth()) / 2,
            (hei - logo.getImageHeight()) / 2 - 100
        );
        
        settingsButton = new TextButton("Settings", skin);
        settingsButton.setPosition(wid - 200f, 0);
        settingsButton.setSize(200, 80);
        
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings();
            }
        });
        
        diceAtlas = new TextureAtlas(Gdx.files.internal("dice.atlas"));
        dice = new Animation<>(1 / 2f, diceAtlas.getRegions());
        
        Gdx.input.setInputProcessor(stage);
        stage.addActor(text);
        stage.addActor(logo);
        stage.addActor(settingsButton);
    }

    /**
     * Creates a settings dialog.
     */
    public void settings() {
        new SettingsDialog().show(stage);
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