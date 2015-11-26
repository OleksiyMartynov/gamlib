package com.lex.gamelib;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public class ResourceManager {
    private static ResourceManager instance;
    private SimpleBaseGameActivity activity;


    private Map<String,BitmapTextureAtlas> textureAtlasMap;
    private Map<String,ITextureRegion> textureRegionMap;
    private Map<String,TiledTextureRegion> tiledTextureRegionMap;
    private Map<String,Font> fontMap;

    private ResourceManager(){
        textureAtlasMap = new HashMap<>();
        textureRegionMap = new HashMap<>();
        tiledTextureRegionMap = new HashMap<>();
        fontMap = new HashMap<>();
    }

    public static ResourceManager getInstance(){
        if(instance == null){
            instance = new ResourceManager();
        }
        return instance;
    }

    public void init(SimpleBaseGameActivity activity){
        instance.activity = activity;
    }

    public SimpleBaseGameActivity getActivity(){
        return instance.activity;
    }

    public void loadResources(){
        //todo:continue
    }


    public interface IResourceLoadStatus{
        void onUpdate(float percent,String info);
        void onComplete();
    }
}
