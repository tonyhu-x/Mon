package com.taj.ourmonopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The GUI representation of a game.
 */
public class GameScreen extends ScreenAdapter {

    // these values preserve the aspect ratio of the map`
    final float worldWidth = 270;
    final float worldHeight = 390;

    GameApp game;
    GameInstance instance;
    Stage stage;
    Camera camera;
    boolean zoomed;
    ArrayList<BlockImage> blockImages;

    public GameScreen(GameApp game) {
        this.game = game;
        this.instance = new GameInstance();
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
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(worldWidth, worldHeight, camera));
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        stage.addListener(new InputListener() {
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

        blockImages.forEach(stage::addActor);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.act(delta);
        stage.draw();
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