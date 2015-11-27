package com.lex.gamelib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.lex.gamelib.FileDescriptions.DescriptionProvider;
import com.lex.gamelib.FileDescriptions.FileDescriptionProvider;
import com.lex.gamelib.FileDescriptions.FontDescription;
import com.lex.gamelib.FileDescriptions.TiledTextureDescription;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public class ResourceManager {

    private static ResourceManager instance;
    private boolean isLoaded = false;
    private SimpleBaseGameActivity activity;
    private List<BitmapTextureAtlas> textureAtlasList;
    private Map<String,ITextureRegion> textureRegionMap;
    private Map<String,TiledTextureRegion> tiledTextureRegionMap;
    private Map<String, Sound> soundMap;
    private Map<String,Font> fontMap;
    private ResourceManager(){
        textureAtlasList = new ArrayList<>();
        textureRegionMap = new HashMap<>();
        tiledTextureRegionMap = new HashMap<>();
        fontMap = new HashMap<>();
        soundMap = new HashMap<>();
    }

    public static ResourceManager getInstance(){
        if(instance == null){
            instance = new ResourceManager();
        }
        return instance;
    }

    public void init(SimpleBaseGameActivity activity) {
        instance.activity = activity;
    }

    public SimpleBaseGameActivity getActivity() {
        return instance.activity;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void loadResources(IResourceLoadStatus listener) throws Exception {
        if (isLoaded) {
            throw new Exception("Resources already loaded.");
        }
        if (listener != null) {
            listener.onUpdate(0, getActivity().getString(R.string.loading_images));
        }
        loadImages();
        if (listener != null) {
            listener.onUpdate(33, getActivity().getString(R.string.loading_sounds));
        }
        loadSounds();
        if (listener != null) {
            listener.onUpdate(66, getActivity().getString(R.string.loading_fonts));
        }
        loadFonts();
        if (listener != null) {
            listener.onUpdate(100, getActivity().getString(R.string.loading_done));
        }
        isLoaded = true;
        if (listener != null) {
            listener.onComplete();
        }
    }

    public void loadResources() throws Exception {
        loadResources(null);
    }

    private void loadFonts() throws IOException, JSONException {
        FileDescriptionProvider fontInfo = new FileDescriptionProvider(getActivity(), DescriptionProvider.FileDescriptionTypes.FontFileDescription);
        FontFactory.setAssetBasePath(AssetFolders.Fonts + "/");
        String[] fontNames = getAssetNameList(AssetFolders.Sounds.toString());
        for (String fontName : fontNames) {
            FontDescription fd = (FontDescription) fontInfo.getDescriptionForFile(fontName);
            ITexture fontTexture2 = new BitmapTextureAtlas(getActivity().getTextureManager(), fd.getFontSpriteWidth(), fd.getFontSpriteHeight(), TextureOptions.BILINEAR);
            Font font = FontFactory.createStrokeFromAsset(getActivity().getFontManager(), fontTexture2, getActivity().getAssets(), fontName, fd.getFontSize(), true, fd.getFontRGB(), fd.getFontStrokeSize(), fd.getFontStrokeRGB());
            font.load();
            fontMap.put(fontName, font);
        }
    }

    private void loadSounds() throws IOException {
        String[] soundNames = getAssetNameList(AssetFolders.Sounds.toString());
        for (String soundName : soundNames) {
            soundMap.put(soundName, SoundFactory.createSoundFromAsset(getActivity().getEngine().getSoundManager(), getActivity(), AssetFolders.Sounds.toString() + "/" + soundName));
        }
    }

    private void loadImages() throws IOException, JSONException {
        loadTextures();
        loadTiledTextures();
    }

    private void loadTiledTextures() throws IOException, JSONException {
        FileDescriptionProvider fontInfo = new FileDescriptionProvider(getActivity(), DescriptionProvider.FileDescriptionTypes.TiledTexturesFileDescription);
        String path = AssetFolders.Images + "/" + ImageFolders.TiledTextures;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
        String[] tiledTexturesNames = getAssetNameList(path);
        for (String tiledTexturesName : tiledTexturesNames) {
            TiledTextureDescription ttd = (TiledTextureDescription) fontInfo.getDescriptionForFile(tiledTexturesName);
            Pair<Integer, Integer> imgSize = getImageSize(path);
            BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), imgSize.first, imgSize.second, TextureOptions.BILINEAR);
            TiledTextureRegion bitmapRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, getActivity(), tiledTexturesName, 0, 0, ttd.getTilesColumnCount(), ttd.getTilesRowCount());
            textureAtlasList.add(textureAtlas);
            tiledTextureRegionMap.put(tiledTexturesName, bitmapRegion);
        }
    }

    private void loadTextures() throws IOException {
        String path = AssetFolders.Images + "/" + ImageFolders.Textures;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
        String[] texturesNames = getAssetNameList(path);
        for (String texturesName : texturesNames) {
            Pair<Integer, Integer> imgSize = getImageSize(path);
            BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), imgSize.first, imgSize.second, TextureOptions.BILINEAR);
            ITextureRegion bitmapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, getActivity(), texturesName, 0, 0);
            textureAtlasList.add(textureAtlas);
            textureRegionMap.put(texturesName, bitmapRegion);
        }
    }

    private String[] getAssetNameList(String path) throws IOException {
        return getActivity().getAssets().list(path);
    }

    private Pair<Integer, Integer> getImageSize(String filePath) throws IOException {
        InputStream istr;
        Bitmap bitmap = null;
        istr = getActivity().getAssets().open(filePath);
        bitmap = BitmapFactory.decodeStream(istr);
        Pair<Integer, Integer> response = new Pair<>(bitmap.getWidth(), bitmap.getHeight());
        bitmap.recycle();
        return response;
    }

    public void unloadResouces() {
        for (BitmapTextureAtlas atlas : textureAtlasList) {
            atlas.unload();
        }
    }

    public enum AssetFolders {Images, Sounds, Fonts}

    public enum ImageFolders {Textures, TiledTextures}

    public interface IResourceLoadStatus{
        void onUpdate(float percent,String info);
        void onComplete();
    }
}
