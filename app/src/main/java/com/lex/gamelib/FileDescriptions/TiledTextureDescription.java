package com.lex.gamelib.FileDescriptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oleksiy on 11/26/2015.
 */
public class TiledTextureDescription extends FileDescription {
    public static final String TEXTURE_DESCRIPTION_FILE = "TiledTextureDescription.json";
    private String imageName;
    private int tilesRowCount;
    private int tilesColumnCount;
    private int animationSpeed;

    public TiledTextureDescription(String jsonString) throws JSONException {
        JSONObject reader = new JSONObject(jsonString);
        init(reader);
    }

    public TiledTextureDescription(JSONObject jsonObject) throws JSONException {
        init(jsonObject);
    }

    private void init(JSONObject jsonObject) throws JSONException {
        imageName = jsonObject.getString(TiledTextureAttr.imageName.toString());
        tilesColumnCount = jsonObject.getInt(TiledTextureAttr.tilesColumnCount.toString());
        tilesRowCount = jsonObject.getInt(TiledTextureAttr.tilesRowCount.toString());
        animationSpeed = jsonObject.getInt(TiledTextureAttr.animationSpeed.toString());
    }

    public String getImageName() {
        return imageName;
    }

    public int getTilesRowCount() {
        return tilesRowCount;
    }

    public int getTilesColumnCount() {
        return tilesColumnCount;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    @Override
    public String getFileName() {
        return imageName;
    }

    @Override
    public DescriptionProvider.FileDescriptionTypes getType() {
        return DescriptionProvider.FileDescriptionTypes.TiledTexturesFileDescription;
    }

    @Override
    public boolean equals(DescriptionProvider.FileDescriptionTypes type) {
        return type == getType();
    }

    public enum TiledTextureAttr {imageName, tilesRowCount, tilesColumnCount, animationSpeed}
}
