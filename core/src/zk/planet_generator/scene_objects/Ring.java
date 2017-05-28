package zk.planet_generator.scene_objects;

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

    public void setZTilt(float tilt) {
        for(Orbiter ring : objects) {
            ring.setZTilt(tilt);
        }
    }

    public void setAngularVelocity(float vel) {
        for(Orbiter ring : objects) {
            ring.setAngularVelocity(vel);
        }
    }

    public void setXTilt(float tilt) {
        for(Orbiter ring : objects) {
            ring.setXTilt(tilt);
        }
    }

    public void setMinimumRadius(float range) {

    }
}
