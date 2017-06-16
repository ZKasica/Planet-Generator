package zk.planet_generator.scene_objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import zk.planet_generator.ColorGroup;

import java.awt.*;

public class Ring implements Json.Serializable {
    private Array<Orbiter> objects;
    private ColorGroup colors;

    private float minRadius;
    private float maxRadius;
    private float angularVelocity;
    private float zTilt;
    private float xTilt;
    private int baseObjectCount;

    private Ring() {

    }

    public Ring(Array<Orbiter> objects) {
        this.objects = objects;
        this.angularVelocity = objects.first().getAngularVelocity();
        this.zTilt = objects.first().getZTilt();
        this.xTilt = objects.first().getXTilt();
    }

    public Ring(Array<Orbiter> objects, ColorGroup colors, float minRadius, float maxRadius) {
        this.objects = objects;
        this.colors = colors;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        this.angularVelocity = objects.first().getAngularVelocity();
        this.zTilt = objects.first().getZTilt();
        this.xTilt = objects.first().getXTilt();
    }

    public Array<Orbiter> getObjects() {
        return objects;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float vel) {
        this.angularVelocity = vel;
        for(Orbiter ring : objects) {
            ring.setAngularVelocity(vel);
        }
    }

    public float getZTilt() {
        return zTilt;
    }

    public void setZTilt(float tilt) {
        this.zTilt = tilt;
        for(Orbiter ring : objects) {
            ring.setZTilt(tilt);
        }
    }

    public float getXTilt() {
        return xTilt;
    }

    public void setXTilt(float tilt) {
        this.xTilt = tilt;
        for(Orbiter ring : objects) {
            ring.setXTilt(tilt);
        }
    }

    public void setRadius(float radius) {
        for(Orbiter ringObj : objects) {
            ringObj.setRadius(radius);
        }
    }

    public float getMinimumRadius() {
        return minRadius;
    }

    public void setMinimumRadius(float minimumRadius) {
        this.minRadius = minimumRadius;
        if(maxRadius < minRadius) {
            maxRadius = minRadius + 1;
        }
        for(Orbiter ringObj : objects) {
            ringObj.setRadius(MathUtils.random(minRadius, maxRadius));
        }
    }

    public float getMaximumRadius() {
        return maxRadius;
    }

    public void setMaximumRadius(float maximumRadius) {
        this.maxRadius = maximumRadius;
        if(minRadius > maxRadius) {
            minRadius = maxRadius - 1;
        }

        for(Orbiter ringObj : objects) {
            ringObj.setRadius(MathUtils.random(minRadius, maxRadius));
        }
    }

    public void setColor(int index, int color) {
        for(Orbiter ringObj : objects) {
            if(ringObj.getColor() == colors.at(index)) {
                ringObj.setColor(color);
            }
        }
        colors.set(index, color);
    }

    public int getObjectCount() {
        return objects.size;
    }

    public int getBaseObjectCount() {
        return baseObjectCount;
    }

    public ColorGroup getColors() {
        return colors;
    }

    public void addObject(Orbiter orbiter) {
        objects.add(orbiter);
    }

    public Orbiter removeObject() {
        return objects.pop();
    }

    @Override
    public void write(Json json) {
        json.writeValue("minimumRadius", minRadius);
        json.writeValue("maximumRadius", maxRadius);
        json.writeValue("angularVelocity", angularVelocity);
        json.writeValue("zTilt", zTilt);
        json.writeValue("xTilt", xTilt);
        json.writeValue("objectCount", objects.size);
        json.writeValue("colorGroup", colors);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        minRadius = json.readValue("minimumRadius", Float.class, jsonData);
        maxRadius = json.readValue("maximumRadius", Float.class, jsonData);
        angularVelocity = json.readValue("angularVelocity", Float.class, jsonData);
        zTilt = json.readValue("zTilt", Float.class, jsonData);
        xTilt = json.readValue("xTilt", Float.class, jsonData);
        colors = json.readValue("colorGroup", ColorGroup.class, jsonData);

        this.baseObjectCount = json.readValue("objectCount", Integer.class, jsonData);
        objects = new Array<>(baseObjectCount);
    }
}
