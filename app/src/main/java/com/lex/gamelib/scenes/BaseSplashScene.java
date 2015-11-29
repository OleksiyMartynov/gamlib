package com.lex.gamelib.scenes;

import com.lex.gamelib.manager.ResourceManager;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.util.adt.align.HorizontalAlign;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public abstract class BaseSplashScene extends BaseScene implements ResourceManager.IResourceLoadStatus {

    protected Sprite backgroundSprite;
    protected AnimatedSprite spinner;
    protected Font textFont;
    protected Text loadingText;
    private PhysicsWorld world;

    void setSplashBackground(Sprite bg) {
        backgroundSprite = bg;
    }

    public void setSplashFont(Font textFont) {
        this.textFont = textFont;
    }

    public void setSplashSpinner(AnimatedSprite spinner) {
        this.spinner = spinner;
    }

    @Override
    public void createScene() {

        //engine.registerUpdateHandler(new FPSLogger());
        if (backgroundSprite != null) {
            attachChild(backgroundSprite);
        }
        if (spinner != null) {
            attachChild(spinner);
        }
        if (textFont != null) {
            loadingText = new Text(0, 0, textFont, "......................................................................", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
            float width = camera.getWidth();
            float height = camera.getHeight();
            if (spinner != null) {
                loadingText.setPosition(width / 2, spinner.getY() - spinner.getHeight() / 2 - 10);
            } else {
                loadingText.setPosition(width / 2, height / 2);
            }
            attachChild(loadingText);
        }
    }

    @Override
    public void onBackKeyPressed() {
        activity.onBackPressed();
    }

    @Override
    public void onUpdate(float percent, String info) {
        if (loadingText != null) {
            loadingText.setText(info);
        }
    }

}
