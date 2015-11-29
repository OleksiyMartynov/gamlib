package com.lex.gamelib.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public class WorldEntity {

    Sprite sprite;
    Body body;
    FixtureDef bodyFixture;
    PhysicsWorld world;
    BaseScene scene;
    private boolean isDestroyed = false;

    public WorldEntity(String spriteTag, BaseScene scene, Sprite sprite, BodyDef.BodyType bodyType, BodyShape bodyShape, float density, float elasticity, float friction) {
        init(spriteTag, scene, sprite, bodyType, bodyShape, density, elasticity, friction);
    }

    public WorldEntity(BaseScene scene, Sprite sprite, BodyDef.BodyType bodyType) {
        init(null, scene, sprite, bodyType, BodyShape.rectangle, 0, 0, 0);
    }

    private void init(String spriteTag, BaseScene scene, Sprite sprite, BodyDef.BodyType bodyType, BodyShape bodyShape, float density, float elasticity, float friction) {
        this.scene = scene;
        this.world = scene.getWorld();
        this.bodyFixture = PhysicsFactory.createFixtureDef(density, elasticity, friction);
        this.sprite = sprite;
        if (bodyShape == BodyShape.circle) {
            this.body = PhysicsFactory.createCircleBody(world, sprite, bodyType, bodyFixture);
        } else {
            this.body = PhysicsFactory.createBoxBody(world, sprite, bodyType, bodyFixture);
        }
        this.body.setUserData(this);
        if (spriteTag != null) {
            this.sprite.setUserData(spriteTag);
        }
    }

    public void present() {
        present(true);
    }

    public void present(boolean updateRotation) {
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

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public FixtureDef getBodyFixture() {
        return bodyFixture;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public enum BodyShape {circle, rectangle}

}
