package com.taj.mon;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class IntroScreen extends ScreenAdapter {
    
    private GameApp game;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextField[] fields;
    private CheckBox[] checkBoxes;
    private TextButton startButton;

    public IntroScreen(GameApp game) {
        this.game = game;
	}

	@Override
    public void show() {
        stage = new Stage();    
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(skin);
        table.setPosition(100, Gdx.graphics.getHeight() / 2f);
        table.left();
        table.columnDefaults(0).size(300, 50).spaceBottom(10);
        table.columnDefaults(1).size(50, 50);
        fields = new TextField[4];
        checkBoxes = new CheckBox[4];

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new TextField("Player " + (i + 1), skin);
            fields[i].setAlignment(Align.center);
            checkBoxes[i] = new CheckBox("", skin);
            checkBoxes[i].setChecked(true);
            table.add(fields[i]);
            table.add(checkBoxes[i]);
            table.row();
        }

        startButton = new TextButton("Start", skin);
        startButton.setSize(300, 100);
        startButton.center();
        startButton.left();
        startButton.setPosition(500, Gdx.graphics.getHeight() / 2f);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ArrayList<String> names = new ArrayList<>();
                for (int i = 0; i < fields.length; i++) {
                    if (checkBoxes[i].isChecked()) {
                        names.add(fields[i].getText());
                    }
                }
                // of course there have to be at least two players
                if (names.size() < 2) {
                    return;
                }
                String[] arr = new String[names.size()];
                names.toArray(arr);
                game.setScreen(new GameScreen(game, arr));
            }
        });

        stage.addActor(startButton);
        stage.addActor(table);

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