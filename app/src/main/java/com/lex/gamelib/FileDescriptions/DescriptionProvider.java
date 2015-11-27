package com.lex.gamelib.FileDescriptions;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Oleksiy on 11/26/2015.
 */
public class DescriptionProvider {

    private String jsonString;

    public DescriptionProvider(Context c, String fileName) throws IOException {
        InputStream is = c.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        jsonString = new String(buffer, "UTF-8");
    }

    public String getJsonString() {
        return jsonString;
    }

    public enum FileDescriptionTypes {FontFileDescription, TiledTexturesFileDescription}
}
