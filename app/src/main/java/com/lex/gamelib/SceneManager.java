package com.lex.gamelib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public class SceneManager {

    private static SceneManager instance;

    public enum SceneType {Splash, Menu, Game, Settings}

    public BaseScene currentScene;
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
    public void setScene(SceneType sceneType)throws ClassNotFoundException, IllegalAccessException, InstantiationException{
        setScene(createScene(sceneType.toString()+"Scene"));
    }

    public BaseScene getCurrentScene(){
        return currentScene;
    }

    public SceneType getCurrentSceneType(){
        for(SceneType t : SceneType.values()){
            if(t.toString().equals(currentScene.getClass().getSimpleName().toString())){
                return t;
            }
        }
        return null;
    }

    private void setScene(BaseScene scene){
        ResourceManager.getInstance().getActivity().getEngine().setScene(scene);
        currentScene = scene;
    }

    private BaseScene createScene(String s) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        BaseScene initializedScene = getInitializedScene(s);
        if(initializedScene == null) {
            Class<?> clazz = Class.forName(s);
            initializedScene = (BaseScene) clazz.newInstance();
            initializedScenes.add(initializedScene);
            return initializedScene;
        }else{
            initializedScene.resetScene();
            return initializedScene;
        }
    }

    private BaseScene getInitializedScene(String s){
        for(BaseScene baseScene : initializedScenes){
            if(baseScene.getClass().getSimpleName().equals(s)){
                return baseScene;
            }
        }
        return null;
    }
}
