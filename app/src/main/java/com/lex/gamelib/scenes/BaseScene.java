package com.lex.gamelib.scenes;

import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.manager.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
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

    public abstract void resetScene();

    public abstract void disposeScene();

    public int getScreenWidth(){
        return (int)camera.getWidth();
    }

    public int getScreenHeight(){
        return (int)camera.getHeight();
    }
}
