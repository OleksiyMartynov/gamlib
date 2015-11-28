package com.lex.gamelib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.lex.gamelib.FileDescriptions.DescriptionProvider;
import com.lex.gamelib.FileDescriptions.FileDescriptionProvider;
import com.lex.gamelib.FileDescriptions.FontDescription;
import com.lex.gamelib.FileDescriptions.TiledTextureDescription;
import com.lex.gamelib.custom.TimedTiledTextureRegion;

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
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksiy on 11/25/2015.
 */
public class ResourceManager {

    private static ResourceManager instance;
    private boolean isLoaded = false;
    private BaseGameActivity activity;
    private List<String> extensionToIgnore;
    private List<BitmapTextureAtlas> textureAtlasList;
    private Map<String,ITextureRegion> textureRegionMap;
    private Map<String, TimedTiledTextureRegion> animationTextureRegionMap;
    private Map<String, Sound> soundMap;
    private Map<String,Font> fontMap;
    private ResourceManager(){
        textureAtlasList = new ArrayList<>();
        textureRegionMap = new HashMap<>();
        animationTextureRegionMap = new HashMap<>();
        fontMap = new HashMap<>();
        soundMap = new HashMap<>();
        extensionToIgnore = new ArrayList<>();
        extensionToIgnore.add(".json");

    }

    public static ResourceManager getInstance(){
        if(instance == null){
            instance = new ResourceManager();
        }
        return instance;
    }

    public void init(BaseGameActivity activity) {
        instance.activity = activity;
    }

    public BaseGameActivity getActivity() {
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
        FileDescriptionProvider fontInfo = new FileDescriptionProvider(getActivity(), DescriptionProvider.FileDescriptionTypes.FontFileDescription, AssetFolders.Fonts + "/" + FontDescription.FONT_DESCRIPTION_FILE);
        FontFactory.setAssetBasePath(AssetFolders.Fonts + "/");
        List<String> fontNames = getAssetNameList(AssetFolders.Sounds.toString(), extensionToIgnore);
        for (String fontName : fontNames) {
            FontDescription fd = (FontDescription) fontInfo.getDescriptionForFile(fontName);
            ITexture fontTexture2 = new BitmapTextureAtlas(getActivity().getTextureManager(), fd.getFontSpriteWidth(), fd.getFontSpriteHeight(), TextureOptions.BILINEAR);
            Font font = FontFactory.createStrokeFromAsset(getActivity().getFontManager(), fontTexture2, getActivity().getAssets(), fontName, fd.getFontSize(), true, fd.getFontRGB(), fd.getFontStrokeSize(), fd.getFontStrokeRGB());
            font.load();
            fontMap.put(fontName, font);
        }
    }

    private void loadSounds() throws IOException {
        List<String> soundNames = getAssetNameList(AssetFolders.Sounds.toString(), extensionToIgnore);
        for (String soundName : soundNames) {
            soundMap.put(soundName, SoundFactory.createSoundFromAsset(getActivity().getEngine().getSoundManager(), getActivity(), AssetFolders.Sounds.toString() + "/" + soundName));
        }
    }

    private void loadImages() throws IOException, JSONException {
        loadTextures();
        loadTiledTextures();
    }

    private void loadTiledTextures() throws IOException, JSONException {
        String path = AssetFolders.Images + "/" + ImageFolders.TiledTextures;
        FileDescriptionProvider fontInfo = new FileDescriptionProvider(getActivity(), DescriptionProvider.FileDescriptionTypes.TiledTexturesFileDescription, path + "/" + TiledTextureDescription.TEXTURE_DESCRIPTION_FILE);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path + "/");
        List<String> tiledTexturesNames = getAssetNameList(path, extensionToIgnore);
        for (String tiledTexturesName : tiledTexturesNames) {
            TiledTextureDescription ttd = (TiledTextureDescription) fontInfo.getDescriptionForFile(tiledTexturesName);
            Pair<Integer, Integer> imgSize = getImageSize(path + "/" + tiledTexturesName);
            BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), imgSize.first, imgSize.second, TextureOptions.BILINEAR);
            TiledTextureRegion bitmapRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, getActivity(), tiledTexturesName, 0, 0, ttd.getTilesColumnCount(), ttd.getTilesRowCount());
            TimedTiledTextureRegion animationTexture = new TimedTiledTextureRegion(ttd.getAnimationSpeed(), bitmapRegion);
            textureAtlasList.add(textureAtlas);
            animationTextureRegionMap.put(tiledTexturesName, animationTexture);
        }
    }

    private void loadTextures() throws IOException {
        String path = AssetFolders.Images + "/" + ImageFolders.Textures;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path + "/");
        List<String> texturesNames = getAssetNameList(path, extensionToIgnore);
        for (String texturesName : texturesNames) {
            Pair<Integer, Integer> imgSize = getImageSize(path + "/" + texturesName);
            BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), imgSize.first, imgSize.second, TextureOptions.BILINEAR);
            ITextureRegion bitmapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, getActivity(), texturesName, 0, 0);
            textureAtlasList.add(textureAtlas);
            textureRegionMap.put(texturesName, bitmapRegion);
        }
    }

    private List<String> getAssetNameList(String path, List<String> ignoreExtension) throws IOException {
        List<String> assets = Arrays.asList(getActivity().getAssets().list(path));
        List<String> response = new ArrayList<>();
        for (String asset : assets) {
            boolean good = true;
            for (String ext : ignoreExtension) {
                if (asset.endsWith(ext)) {
                    good = false;
                }
            }
            if (good) {
                response.add(asset);
            }
        }


        return response;
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
