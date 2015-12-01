package com.lex.gamelib.objects;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

/**
 * Created by Oleksiy on 11/30/2015.
 */
public class WorldJoint {
    private DistanceJointDef joint;
    private PhysicsWorld world;
    private BaseScene scene;
    private WorldEntity one;
    private WorldEntity two;
    private Line line;
    private WorldEntity lineEntity;

    public WorldJoint(BaseScene scene, PhysicsWorld world, final WorldEntity one, final WorldEntity two, final Line line) {
        this.scene = scene;
        joint = new DistanceJointDef();
        joint.initialize(one.getBody(), two.getBody(), one.getBody().getWorldCenter(), two.getBody().getWorldCenter());
        this.world = world;
        this.world.registerPhysicsConnector(new PhysicsConnector(one.getSprite(), one.getBody(), true, true) {
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                final Vector2 twoWorldCenter = two.getBody().getWorldCenter();
                final Vector2 oneWorldCenter = one.getBody().getWorldCenter();
                line.setPosition(oneWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, oneWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
        });
        this.line = line;
    }

    public WorldJoint(BaseScene scene, PhysicsWorld world, final WorldEntity one, final WorldEntity two, String lineImage) {

        this.lineEntity = new WorldEntity.WorldEntityBuilder(lineImage, scene, BodyDef.BodyType.StaticBody, "joint")
                .setDisableCollision(true)
                .build();

        final Vector2 twoWorldCenter = two.getBody().getWorldCenter();
        final Vector2 oneWorldCenter = one.getBody().getWorldCenter();
        lineEntity.getSprite().setX((oneWorldCenter.x + twoWorldCenter.x) / 2 * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        lineEntity.getSprite().setY((oneWorldCenter.y + twoWorldCenter.y) / 2 * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        lineEntity.rotate(oneWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, oneWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

        float width = Vector2Pool.obtain(one.getBody().getWorldCenter().x - two.getBody().getWorldCenter().x, one.getBody().getWorldCenter().y - two.getBody().getWorldCenter().y).len() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        float sHeight = this.lineEntity.getSprite().getHeight() * width / this.lineEntity.getSprite().getWidth();
        this.lineEntity.getSprite().setWidth(width);
        this.lineEntity.getSprite().setHeight(sHeight);

        this.scene = scene;
        joint = new DistanceJointDef();
        joint.initialize(one.getBody(), two.getBody(), one.getBody().getWorldCenter(), two.getBody().getWorldCenter());
        this.world = world;
        this.world.registerPhysicsConnector(new PhysicsConnector(one.getSprite(), one.getBody(), true, true) {
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                final Vector2 twoWorldCenter = two.getBody().getWorldCenter();
                final Vector2 oneWorldCenter = one.getBody().getWorldCenter();
                lineEntity.getSprite().setX((oneWorldCenter.x + twoWorldCenter.x) / 2 * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                lineEntity.getSprite().setY((oneWorldCenter.y + twoWorldCenter.y) / 2 * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                lineEntity.rotate(oneWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, oneWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, twoWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
        });
    }

    public void present() {
        world.createJoint(joint);
        if (line != null) {
            scene.attachChild(line);
        } else {
            lineEntity.present();
        }
    }
    //todo: destory offscreen
    //todo: create builder class
}
