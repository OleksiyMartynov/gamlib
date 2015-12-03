package com.lex.gamelib.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public class WorldEntity implements AnimatedSprite.IAnimationListener {

    Sprite sprite;
    Body body;
    FixtureDef bodyFixture;
    PhysicsWorld world;
    BaseScene scene;
    private boolean isDestroyed = false;
    private boolean updateRotation;
    private boolean isCollisionDisabled;
    private WorldEntityEventListener listener;

    private WorldEntity(String imageFileName, BaseScene scene, BodyDef.BodyType bodyType, BodyShape bodyShape, float startX, float startY, float density, float elasticity, float friction, boolean updateRotation, String spriteTag, boolean disableCollision, boolean destroyWhenOffscreen, WorldEntityEventListener listener) {
        this.sprite = ResourceManager.getInstance().getSprite(imageFileName);
        if (this.sprite == null) {
            this.sprite = ResourceManager.getInstance().getAnimatedSprite(imageFileName, this);
        }
        init(scene, bodyType, bodyShape, startX, startY, density, elasticity, friction, updateRotation, spriteTag, disableCollision, destroyWhenOffscreen, listener);
    }

    private void init(BaseScene scene, BodyDef.BodyType bodyType, BodyShape bodyShape, float startX, float startY, float density, float elasticity, float friction, boolean updateRotation, String spriteTag, boolean disableCollision, boolean destroyWhenOffscreen, WorldEntityEventListener listener) {
        this.sprite.setPosition(startX, startY);
        this.scene = scene;
        this.world = scene.getWorld();
        this.bodyFixture = PhysicsFactory.createFixtureDef(density, elasticity, friction);
        if (bodyShape == BodyShape.circle) {
            this.body = PhysicsFactory.createCircleBody(world, sprite, bodyType, bodyFixture);
        } else {
            this.body = PhysicsFactory.createBoxBody(world, sprite, bodyType, bodyFixture);
        }
        this.body.setUserData(this);
        if (spriteTag != null) {
            this.sprite.setUserData(spriteTag);
        }
        this.updateRotation = updateRotation;
        if (disableCollision) {
            setDisableCollision(disableCollision);
        }
        if (destroyWhenOffscreen) {
            sprite.registerUpdateHandler(getOffscreenHandler());
        }
        this.listener = listener;
    }

    private IUpdateHandler getOffscreenHandler() {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                Camera c = scene.getCamera();
                if (!c.isEntityVisible(getSprite())) {
                    scene.removeEntity(WorldEntity.this);
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    public float getSpriteCenterX() {
        float anchorFaceX = sprite.getX();
        float spriteWidth = sprite.getWidth();
        return anchorFaceX + spriteWidth / 2;
    }

    public float getSpriteCenterY() {
        float anchorFaceY = sprite.getY();
        float spriteHeight = sprite.getHeight();
        return anchorFaceY + spriteHeight / 2;
    }

    public void rotate(float startX, float startY, float targetX, float targetY) {
        float a = 270 + (float) Math.toDegrees(Math.atan2((targetX - sprite.getRotationCenterX() - startX), (targetY - sprite.getRotationCenterY() - startY)));
        sprite.setRotation(a);
    }

    public void setDisableCollision(boolean disable) {
        isCollisionDisabled = disable;
        for (int i = 0; i < body.getFixtureList().size(); i++) {
            this.getBody().getFixtureList().get(i).setSensor(disable);
        }
    }

    public void present() {
        scene.attachChild(sprite);
        world.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, updateRotation));
    }

    public void destroySelf() {
        if (!isDestroyed) {
            world.destroyBody(body);
            sprite.detachSelf();
            sprite.dispose();
            sprite = null;
            body = null;
            bodyFixture = null;
            world = null;
            scene = null;
            isDestroyed = true;
        }
    }

    public boolean isCollisionDissabled() {
        return isCollisionDisabled;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public void setListener(WorldEntityEventListener listener) {
        this.listener = listener;
    }

    public FixtureDef getBodyFixture() {
        return bodyFixture;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    private void notifyListener(Event e, Object data) {
        if (listener != null) {
            listener.onEvent(e, data);
        }
    }

    @Override
    public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

    }

    @Override
    public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {

    }

    @Override
    public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
        notifyListener(Event.animationLoopComplete, null);
    }

    @Override
    public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
        notifyListener(Event.animationFinished, null);
    }

    public enum BodyShape {circle, rectangle}

    public enum Event {animationLoopComplete, animationFinished, destroyed}

    public interface WorldEntityEventListener {
        void onEvent(Event e, Object data);
    }

    public static class WorldEntityBuilder {
        //required
        private String imageFileName;
        private BaseScene scene;
        private BodyDef.BodyType bodyType;
        private String spriteTag;

        //default
        private BodyShape bodyShape = BodyShape.rectangle;
        private float density = 1.0f;
        private float elasticity, friction, positionX, positionY;
        private boolean updateRotation = true;
        private boolean disableCollision = false;
        private boolean destroyWhenOffscreen = false;
        private WorldEntityEventListener listener;


        public WorldEntityBuilder(String imageFileName, BaseScene scene, BodyDef.BodyType bodyType, String spriteTag) {
            this.imageFileName = imageFileName;
            this.scene = scene;
            this.bodyType = bodyType;
            this.spriteTag = spriteTag;
        }

        public WorldEntityBuilder setBodyShape(BodyShape bodyShape) {
            this.bodyShape = bodyShape;
            return this;
        }

        public WorldEntityBuilder setPosition(float x, float y) {
            this.positionX = x;
            this.positionY = y;
            return this;
        }

        public WorldEntityBuilder setDensity(float density) {
            this.density = density;
            return this;
        }

        public WorldEntityBuilder setElasticity(float elasticity) {
            this.elasticity = elasticity;
            return this;
        }

        public WorldEntityBuilder setFriction(float friction) {
            this.friction = friction;
            return this;
        }

        public WorldEntityBuilder setUpdateRotation(boolean updateRotation) {
            this.updateRotation = updateRotation;
            return this;
        }

        public WorldEntityBuilder setDisableCollision(boolean disableCollision) {
            this.disableCollision = disableCollision;
            return this;
        }

        public WorldEntityBuilder setListener(WorldEntityEventListener listener) {
            this.listener = listener;
            return this;
        }

        public WorldEntityBuilder setDestroyWhenOffscreen(boolean destroyWhenOffscreen) {
            this.destroyWhenOffscreen = destroyWhenOffscreen;
            return this;
        }

        public WorldEntity build() {
            return new WorldEntity(imageFileName, scene, bodyType, bodyShape, positionX, positionY, density, elasticity, friction, updateRotation, spriteTag, disableCollision, destroyWhenOffscreen, listener);
        }
    }



}
