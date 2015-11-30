package com.lex.gamelib.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.lex.gamelib.FileDescriptions.DescriptionProvider;
import com.lex.gamelib.FileDescriptions.FileDescriptionProvider;
import com.lex.gamelib.FileDescriptions.FontDescription;
import com.lex.gamelib.FileDescriptions.TiledTextureDescription;
import com.lex.gamelib.R;
import com.lex.gamelib.custom.TimedTiledTextureRegion;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
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

    public void init(BaseGameActivity activity) throws IOException, JSONException {
        instance.activity = activity;
    }

    public BaseGameActivity getActivity() {
        return instance.activity;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void loadResources(final IResourceLoadStatus listener) throws Exception {
        if (isLoaded) {
            throw new Exception("Resources already loaded.");
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                notifyListener(listener, 0, getActivity().getString(R.string.loading_fonts));
                try {
                    loadFonts();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 500);


        Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            public void run() {
                notifyListener(listener, 33, getActivity().getString(R.string.loading_images));
                try {
                    loadImages();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

        Handler handler3 = new Handler(Looper.getMainLooper());
        handler3.postDelayed(new Runnable() {
            public void run() {
                notifyListener(listener, 66, getActivity().getString(R.string.loading_sounds));
                try {
                    loadSounds();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);


        Handler handler4 = new Handler(Looper.getMainLooper());
        handler4.postDelayed(new Runnable() {
            public void run() {
                notifyListener(listener, 99, getActivity().getString(R.string.loading_done));
                isLoaded = true;
                notifyListener(listener, 100, "");
            }
        }, 2000);


        Handler handler5 = new Handler(Looper.getMainLooper());
        handler5.postDelayed(new Runnable() {
            public void run() {
                notifyListener(listener, 100, "");
            }
        }, 2500);

    }

    public void loadResources() throws Exception {
        loadResources(null);
    }

    public void notifyListener(final IResourceLoadStatus listener, final float percent, final String msg) {
        if (listener != null) {
            if (percent < 100) {
                listener.onUpdate(100, msg);
            } else {
                listener.onComplete();
            }
        }
    }

    public ITextureRegion getTextureRegion(String fileName) {
        return textureRegionMap.get(fileName);
    }

    public TimedTiledTextureRegion getAnimationTextureRegion(String fileName) {
        return animationTextureRegionMap.get(fileName);
    }

    public Sound getSound(String fileName) {
        return soundMap.get(fileName);
    }

    public Sprite getSprite(String fileName) {
        ITextureRegion tr = getTextureRegion(fileName);
        if(tr == null)
        {
            return null;
        }
        return new Sprite(0, 0, tr , getActivity().getVertexBufferObjectManager());
    }

    public AnimatedSprite getAnimatedSprite(String fileName) {
        return getAnimatedSprite(fileName,null);
    }

    public AnimatedSprite getAnimatedSprite(String fileName, AnimatedSprite.IAnimationListener animationListener) {
        TimedTiledTextureRegion timedTiledTextureRegion = getAnimationTextureRegion(fileName);
        AnimatedSprite animatedSprite = new AnimatedSprite(0.0f, 0.0f, timedTiledTextureRegion.getTextureRegion(), getActivity().getVertexBufferObjectManager());
        animatedSprite.animate(timedTiledTextureRegion.getAnimationSpeed(),animationListener);
        return animatedSprite;
    }

    public Font getFont(String fileName) {
        return fontMap.get(fileName);
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
            textureAtlas.load();
        }
    }

    private void loadTextures() throws IOException {
        String path = AssetFolders.Images + "/" + ImageFolders.Textures;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path + "/");
        List<String> texturesNames = getAssetNameList(path, extensionToIgnore);
        for (String texturesName : texturesNames) {
            ITexture mTexture = readTexture(path, texturesName);
            mTexture.load();
            ITextureRegion bitmapRegion = TextureRegionFactory.extractFromTexture(mTexture);
            textureRegionMap.put(texturesName, bitmapRegion);
        }
    }

    private ITexture readTexture(final String path, final String texturesName) throws IOException {
        return new BitmapTexture(getActivity().getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getActivity().getAssets().open(path + "/" + texturesName);
            }
        });
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

    public void unloadResources() {
        for (BitmapTextureAtlas atlas : textureAtlasList) {
            atlas.unload();
        }
    }

    public enum AssetFolders {Images, Sounds, Fonts}

    public enum ImageFolders {Textures, TiledTextures}

    public interface IResourceLoadStatus{
        void onUpdate(float percent,String info);
        void onComplete();

        void onFailed();
    }

}
