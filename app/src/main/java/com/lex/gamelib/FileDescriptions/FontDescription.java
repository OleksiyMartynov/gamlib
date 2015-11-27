package com.lex.gamelib.FileDescriptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Oleksiy on 11/26/2015.
 */
public class FontDescription extends FileDescription {
    public final static String FONT_DESCRIPTION_FILE = "FontDescription.json";
    private String fontName;
    private int fontSize;
    private int fontRGB;
    private int fontStrokeSize;
    private int fontStrokeRGB;
    private int fontSpriteWidth;
    private int fontSpriteHeight;

    public FontDescription(String jsonString) throws IOException, JSONException {
        JSONObject reader = new JSONObject(jsonString);
        init(reader);

    }

    public FontDescription(JSONObject jsonObject) throws JSONException {
        init(jsonObject);
    }

    private void init(JSONObject jsonObject) throws JSONException {
        fontSpriteWidth = jsonObject.getInt(FontAttr.fontSpriteWidth.toString());
        fontSpriteHeight = jsonObject.getInt(FontAttr.fontSpriteHeight.toString());
        fontName = jsonObject.getString(FontAttr.fontName.toString());
        fontSize = jsonObject.getInt(FontAttr.fontSize.toString());
        fontRGB = jsonObject.getInt(FontAttr.fontRGB.toString());
        fontStrokeSize = jsonObject.getInt(FontAttr.fontStrokeSize.toString());
        fontStrokeRGB = jsonObject.getInt(FontAttr.fontStrokeRGB.toString());
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFontRGB() {
        return fontRGB;
    }

    public int getFontStrokeSize() {
        return fontStrokeSize;
    }

    public int getFontStrokeRGB() {
        return fontStrokeRGB;
    }

    public int getFontSpriteWidth() {
        return fontSpriteWidth;
    }

    public int getFontSpriteHeight() {
        return fontSpriteHeight;
    }

    @Override
    public String getFileName() {
        return getFontName();
    }

    @Override
    public DescriptionProvider.FileDescriptionTypes getType() {
        return DescriptionProvider.FileDescriptionTypes.FontFileDescription;
    }

    @Override
    public boolean equals(DescriptionProvider.FileDescriptionTypes type) {
        return type == getType();
    }

    public enum FontAttr {fontSpriteWidth, fontSpriteHeight, fontName, fontSize, fontRGB, fontStrokeSize, fontStrokeRGB}
}
