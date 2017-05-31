package zk.planet_generator.scene_objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import zk.planet_generator.ColorGroup;

/**
 * Created by zach on 5/27/17.
 */
public class Ring {
    private Array<Orbiter> objects;
    private ColorGroup colors;
    private float minRadius;
    private float maxRadius;
    private float angularVelocity;
    private float zTilt;
    private float xTilt;

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

    public float getMinRadius() {
        return minRadius;
    }

    public float getMaxRadius() {
        return maxRadius;
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

    public void setMinimumRadius(float minimumRadius) {
        this.minRadius = minimumRadius;
        if(maxRadius < minRadius) {
            maxRadius = minRadius + 1;
        }
        for(Orbiter ringObj : objects) {
            ringObj.setRadius(MathUtils.random(minRadius, maxRadius));
        }

        // TODO: Scale radius of objects accordingly
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

    public int getObjectCount() {
        return objects.size;
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
}
