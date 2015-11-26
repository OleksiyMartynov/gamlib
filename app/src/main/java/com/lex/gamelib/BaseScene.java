package com.lex.gamelib;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public abstract class BaseScene extends Scene {

    protected SimpleBaseGameActivity activity;
    protected Engine engine;
    protected Camera camera;
    protected VertexBufferObjectManager vertexBufferObjectManager;
    protected ResourceManager resourceManager;
    protected SceneManager sceneManager;

    public BaseScene() {
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

    public abstract SceneManager.SceneType getSceneType();

    public abstract void resetScene();

    public abstract void disposeScene();

    public int getScreenWidth(){
        return (int)camera.getWidth();
    }

    public int getScreenHeight(){
        return (int)camera.getHeight();
    }
}
