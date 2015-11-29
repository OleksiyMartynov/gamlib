package com.lex.gamelib.scenes;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.manager.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public abstract class BaseScene extends Scene {

    protected BaseGameActivity activity;
    protected Engine engine;
    protected Camera camera;
    protected VertexBufferObjectManager vertexBufferObjectManager;
    protected ResourceManager resourceManager;
    protected SceneManager sceneManager;
    protected PhysicsWorld world;

    public BaseScene() {
        world = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH), false);
        registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                world.onUpdate(pSecondsElapsed);
            }

            @Override
            public void reset() {

            }
        });
        resourceManager = ResourceManager.getInstance();
        activity = resourceManager.getActivity();
        vertexBufferObjectManager = activity.getVertexBufferObjectManager();
        engine = activity.getEngine();
        camera = engine.getCamera();
        sceneManager = SceneManager.getInstance();
        createScene();
    }

    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract void resetScene();

    public abstract void disposeScene();

    public float getCameraWidth() {
        return camera.getWidth();
    }

    public float getCameraHeight() {
        return camera.getHeight();
    }

    public PhysicsWorld getWorld() {
        return world;
    }
}
