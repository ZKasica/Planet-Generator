package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import zk.planet_generator.ColorGroup;
import zk.planet_generator.Scene;

public class Orbiter extends SpaceObject {
    private float angularVelocity;
    private float zTilt; // Affects how steep of an angle it orbits at
    private float xTilt; // Affects how vertical it appears to the viewer
    private float angle;
    private float radius;
    private float yOffset;

    private Vector3 position;
    private Matrix3 rotZ;
    private Matrix3 rotX;

    private Orbiter() {

    }

    public Orbiter(Sprite sprite, OrbiterBlueprint blueprint) {
        this(sprite, blueprint, Color.rgba8888(Color.WHITE));
    }

    public Orbiter(Sprite sprite, OrbiterBlueprint blueprint, int color) {
        super(sprite, color);

        this.angularVelocity = blueprint.angularVelocity;
        this.zTilt = blueprint.zTilt;
        this.xTilt = blueprint.xTilt;
        this.angle = blueprint.angle;
        this.radius = blueprint.radius;
        this.yOffset = blueprint.yOffset;

//        setColor(color);

        initializeMatrices();
    }

    private void initializeMatrices() {
        position = new Vector3();
        rotZ = new Matrix3().setToRotation(zTilt);
        rotX = new Matrix3().setToRotation(Vector3.X, xTilt);
    }

    @Override
    public void update(float delta) {
        angle = (angle + (angularVelocity * delta)) % 360;

        // Start object on the XZ plane and rotate to correct 3D orientation
        position.set(radius * MathUtils.cosDeg(angle), 0, radius * MathUtils.sinDeg(angle));
        position.mul(rotZ);
        position.mul(rotX);

        // Set sprite position for 2D rendering and zCoord for ordering
        getSprite().setPosition(Scene.CENTER_X - getSprite().getWidth()/2 + position.x, Scene.CENTER_Y - getSprite().getHeight()/2 + position.y + yOffset);
        setZPos((int)position.z);
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getZTilt() {
        return zTilt;
    }

    public void setZTilt(float amount) {
        zTilt = amount;
        rotZ.setToRotation(zTilt);
    }

    public float getXTilt() {
        return xTilt;
    }

    public void setXTilt(float xTilt) {
        this.xTilt = xTilt;
        rotX.setToRotation(Vector3.X, xTilt);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getSpriteSize() {
        return getSprite().getWidth();
    }

    public float getAngle() {
        return angle;
    }

    public static class OrbiterBlueprint {
        public float angularVelocity;
        public float zTilt;
        public float xTilt;
        public float angle;
        public float radius;
        public float yOffset;
    }


    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("angularVelocity", angularVelocity);
        json.writeValue("zTilt", zTilt);
        json.writeValue("xTilt", xTilt);
        json.writeValue("radius", radius);
        json.writeValue("angle", angle);
        json.writeValue("yOffset", yOffset);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        angularVelocity = json.readValue("angularVelocity", Float.class, jsonData);
        zTilt = json.readValue("zTilt", Float.class, jsonData);
        xTilt = json.readValue("xTilt", Float.class, jsonData);
        radius = json.readValue("radius", Float.class, jsonData);
        angle = json.readValue("angle", Float.class, jsonData);
        yOffset = json.readValue("yOffset", Float.class, jsonData);
        initializeMatrices();
    }
}
