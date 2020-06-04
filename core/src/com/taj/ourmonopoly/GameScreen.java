package com.taj.ourmonopoly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {

    // these values preserve the aspect ratio of the map`
    final float worldWidth = 270;
    final float worldHeight = 390;

    GameApp game;
    boolean zoomed;
    Stage stage;
    Camera camera;
    
    public GameScreen(GameApp game) {
        this.game = game;    
    }

    @Override
    public void show() {
        zoomed = true;
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(worldWidth, worldHeight * GameApp.ASPECT_RATIO, camera));
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.Q: case Input.Keys.E:
                        zoom();
                        return true;                        
                    default:
                        return false;
                }
            }
        });

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
            camera.viewportHeight = worldHeight * GameApp.ASPECT_RATIO;
        }
        else {
            camera.viewportWidth = worldWidth / 2;
            camera.viewportHeight = worldHeight / 2 * GameApp.ASPECT_RATIO;
        }

        zoomed = !zoomed;
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }
}