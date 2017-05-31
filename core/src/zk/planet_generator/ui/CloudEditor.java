package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.sun.corba.se.internal.iiop.ORB;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Cloud;
import zk.planet_generator.scene_objects.Orbiter;

/**
 * Created by zach on 5/29/17.
 */
public class CloudEditor extends ObjectEditor {
    private Array<Cloud> clouds;
    private float velocity;

    public CloudEditor(Scene scene, String objectName, Array<Cloud> clouds) {
        super(scene, objectName);
        this.clouds = clouds;

        float[] zeroSnap = new float[]{0};

        createSlider("Count", 0, 100, null, clouds.size, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int count = (int) ((VisSlider) actor).getValue();

                if(count > clouds.size) {
                    for(int i = 0; i < count - clouds.size; i++) {
                        Cloud cloud = scene.getObjectGenerator().createCloud(velocity);
                        clouds.add(cloud);
                    }
                } else {
                    for(int i = 0; i < clouds.size - count; i++) {
                        Cloud removed = clouds.pop();
                        scene.removeObjects(removed.getCloudObjects());
                    }
                }
            }
        });

        createSlider("Velocity", -30, 30, zeroSnap, 1, clouds.first().getAngularVelocity(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                velocity = (int) ((VisSlider) actor).getValue();

                for(Cloud cloud : clouds) {
                    for(int i = 0; i < cloud.getCloudObjects().size; i++) {
                        cloud.getCloudObjects().get(i).setAngularVelocity(velocity);
                    }
                }
            }
        });
    }

    @Override
    public void deleteObjects() {
        int size = clouds.size;
        for(int i = 0; i < size; i++) {
            Array<Orbiter> cloudObjects = clouds.pop().getCloudObjects();
            scene.removeObjects(cloudObjects);
        }
        clouds.clear();
    }
}
