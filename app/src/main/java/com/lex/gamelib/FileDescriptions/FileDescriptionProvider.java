package com.lex.gamelib.FileDescriptions;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksiy on 11/26/2015.
 */
public class FileDescriptionProvider extends DescriptionProvider {

    private List<FileDescription> descriptions;

    public FileDescriptionProvider(Context c, FileDescriptionTypes fileType, String descriptionFileName) throws IOException, JSONException {
        super(c, descriptionFileName);
        descriptions = new ArrayList<>();
        Object json = new JSONTokener(getJsonString()).nextValue();
        if (json instanceof JSONObject) {
            parseObject((JSONObject) json, fileType);
        } else if (json instanceof JSONArray) {
            parseArray((JSONArray) json, fileType);
        }
    }

    private void parseArray(JSONArray jsonArray, FileDescriptionTypes fileType) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            parseObject(jsonObject, fileType);
        }
    }

    private void parseObject(JSONObject jsonObject, FileDescriptionTypes fileType) throws JSONException {
        switch (fileType) {
            case FontFileDescription:
                descriptions.add(new FontDescription(jsonObject));
                break;
            case TiledTexturesFileDescription:
                descriptions.add(new TiledTextureDescription(jsonObject));
                break;
        }
    }

    public FileDescription getDescriptionForFile(String fileName) {
        for (FileDescription fd : descriptions) {
            if (fd.getFileName().equals(fileName)) {
                return fd;
            }
        }
        return null;
    }
}
