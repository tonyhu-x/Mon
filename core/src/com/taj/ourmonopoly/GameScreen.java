package com.taj.ourmonopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.taj.ourmonopoly.block.Property;
import com.taj.ourmonopoly.dialog.PropertyPurchaseDialog;

/**
 * The GUI representation of a game.
 */
public class GameScreen extends ScreenAdapter {

    // these values preserve the aspect ratio of the map`
    final float worldWidth = 270;
    final float worldHeight = 390;

    private Skin skin;
    private GameApp game;
    private GameInstance instance;
    private Stage mainStage;
    private ArrayList<BlockImage> blockImages;

    Stage uiStage;
    Dialog dialog;
    Label l1, l2, l3, l4;
    TextButton nextButton;
    VerticalGroup group;
    Camera camera;
    boolean zoomed;


    public GameScreen(GameApp game, String[] arr) {
        this.game = game;
        this.instance = new GameInstance(this, arr);
        blockImages = new ArrayList<>();

        try {
            createImages();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void show() {
        zoomed = false;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        camera = new OrthographicCamera();
        mainStage = new Stage(new FitViewport(worldWidth, worldHeight, camera), game.batch);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        mainStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.Q:
                    case Input.Keys.E:
                        zoom();
                        return true;
                    default:
                        return false;
                }
            }
        });
        blockImages.forEach(mainStage::addActor);

        uiStage = new Stage(new FitViewport(GameApp.WINDOW_WIDTH, GameApp.WINDOW_HEIGHT), game.batch);
        l1 = new Label(instance.players.get(0).name + ": " + instance.startingCashAmt, skin);
        l2 = new Label(instance.players.get(1).name + ": " + instance.startingCashAmt, skin);
        l3 = new Label(instance.players.get(2).name + ": " + instance.startingCashAmt, skin);
        l4 = new Label(instance.players.get(3).name + ": " + instance.startingCashAmt, skin);
        group = new VerticalGroup();
        group.addActor(l1);
        group.addActor(l2);
        group.addActor(l3);
        group.addActor(l4);
        group.setBounds(0, 0, 300, 400);
        uiStage.addActor(group);
        
        nextButton = new TextButton("Next Player", skin);
        nextButton.setBounds(1500, 200, 200, 300);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextMove();
            }
        });

        uiStage.addActor(nextButton);

        var input = new InputMultiplexer(mainStage, uiStage);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mainStage.getViewport().apply();
        mainStage.act(delta);
        mainStage.draw();

        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    public void nextMove() {
        instance.nextPlayer();
        // beginning of test code ------
        updateLabels();
        // end of test code ------
    }

    public void zoom() {
        if (zoomed) {
            camera.viewportWidth = worldWidth;
            camera.viewportHeight = worldHeight;
        } else {
            camera.viewportWidth = worldWidth / 2;
            camera.viewportHeight = worldHeight / 2;
        }

        zoomed = !zoomed;
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    public void updateLabels() {
        l1.setText(instance.players.get(0).name + ": " + instance.players.get(0).getCashAmt());
        l2.setText(instance.players.get(1).name + ": " + instance.players.get(1).getCashAmt());
        l3.setText(instance.players.get(2).name + ": " + instance.players.get(2).getCashAmt());
        l4.setText(instance.players.get(3).name + ": " + instance.players.get(3).getCashAmt());
    }

    public void createDialog(String type, Object... args) {
        switch (type) {
            case "PurchaseProperty":
                new PropertyPurchaseDialog(
                    "Purchase Property",
                    skin,
                    (Property) args[0],
                    (Player) args[1]
                ).show(uiStage);
                break;
            default:
                break;
        }
    }

    private void createImages() throws IOException {
        String path = GameApp.PATH_TO_ASSETS + "blockPos.txt";
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            System.exit(-1);
        }

        String row;
        int count = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int x = Integer.parseInt(tokens[0]), y = Integer.parseInt(tokens[1]);
            int rotate = Integer.parseInt(tokens[2]);

            blockImages.add(new BlockImage(instance.blocks.get(count), x, y, rotate));
            count++;
        }

    }
}