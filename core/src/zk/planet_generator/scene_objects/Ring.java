package zk.planet_generator.scene_objects;

import com.badlogic.gdx.utils.Array;

/**
 * Created by zach on 5/27/17.
 */
public class Ring {
    private Array<Orbiter> objects;

    public Ring(Array<Orbiter> objects) {
        this.objects = objects;
    }

    public Array<Orbiter> getObjects() {
        return objects;
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
