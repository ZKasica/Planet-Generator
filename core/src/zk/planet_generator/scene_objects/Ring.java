package zk.planet_generator.scene_objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by zach on 5/27/17.
 */
public class Ring {
    private Array<Orbiter> objects;
    private float minRadius;
    private float maxRadius;

    public Ring(Array<Orbiter> objects, float minRadius, float maxRadius) {
        this.objects = objects;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }

    public Array<Orbiter> getObjects() {
        return objects;
    }

    public float getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
    }

    public float getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
    }

    public float getAngularVelocity() {
        return objects.first().getAngularVelocity();
    }

    public void setAngularVelocity(float vel) {
        for(Orbiter ring : objects) {
            ring.setAngularVelocity(vel);
        }
    }

    public float getZTilt() {
        return objects.first().getZTilt();
    }

    public void setZTilt(float tilt) {
        for(Orbiter ring : objects) {
            ring.setZTilt(tilt);
        }
    }

    public float getXTilt() {
        return objects.first().getXTilt();
    }

    public void setXTilt(float tilt) {
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

    private float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
