package zk.planet_generator.scene_objects;

import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * Created by zach on 5/29/17.
 */
public class Cloud {
    private Array<Orbiter> cloudObjects;

    public Cloud(Array<Orbiter> cloudObjects) {
        this.cloudObjects = cloudObjects;
    }

    public Array<Orbiter> getCloudObjects() {
        return cloudObjects;
    }

    public float getAngularVelocity() {
        return cloudObjects.first().getAngularVelocity();
    }
}
