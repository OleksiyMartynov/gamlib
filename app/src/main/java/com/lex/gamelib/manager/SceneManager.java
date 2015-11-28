package com.lex.gamelib.manager;

import com.lex.gamelib.scenes.BaseScene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public class SceneManager {

    private static SceneManager instance;
    public BaseScene currentScene;

    //public enum SceneType {Splash, Menu, Game, Settings, TestGame}
    private Class[] sceneClasses;
    private List<BaseScene> initializedScenes;

    private SceneManager(){
        initializedScenes = new ArrayList();
    }
    public static SceneManager getInstance(){
        if(instance==null){
            instance = new SceneManager();
        }
        return instance;
    }

    public void init(Class[] sceneClasses) {
        this.sceneClasses = sceneClasses;
    }

    public void setScene(Class classType) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        setScene(createScene(classType));
    }

    public BaseScene getCurrentScene(){
        return currentScene;
    }

    public Class getCurrentSceneType() {
        return currentScene.getClass();
    }

    private void setScene(BaseScene scene){
        ResourceManager.getInstance().getActivity().getEngine().setScene(scene);
        currentScene = scene;
    }

    private BaseScene createScene(Class clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        BaseScene initializedScene = getInitializedScene(clazz);
        if(initializedScene == null) {
            initializedScene = (BaseScene) clazz.newInstance();
            initializedScenes.add(initializedScene);
            return initializedScene;
        }else{
            initializedScene.resetScene();
            return initializedScene;
        }
    }


    private BaseScene getInitializedScene(Class clazz) {
        for(BaseScene baseScene : initializedScenes){
            if (clazz.isInstance(baseScene)) {
                return baseScene;
            }
        }
        return null;
    }
}
