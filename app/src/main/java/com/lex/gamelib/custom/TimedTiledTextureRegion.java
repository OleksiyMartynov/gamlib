package com.lex.gamelib.custom;

import org.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * Created by Oleksiy on 11/27/2015.
 */
public class TimedTiledTextureRegion {
    private int animationSpeed;
    private TiledTextureRegion textureRegion;

    public TimedTiledTextureRegion(int animationSpeed, TiledTextureRegion textureRegion) {
        this.animationSpeed = animationSpeed;
        this.textureRegion = textureRegion;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public TiledTextureRegion getTextureRegion() {
        return textureRegion;
    }
}
