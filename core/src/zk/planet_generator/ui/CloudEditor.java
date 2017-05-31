package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.sun.corba.se.internal.iiop.ORB;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Cloud;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Star;

/**
 * Created by zach on 5/29/17.
 */
public class CloudEditor extends ObjectEditor {
    private Array<Cloud> clouds;
    private VisSlider count;
    private VisSlider velocitySlider;

    private float velocity;

    public CloudEditor(Scene scene, String objectName) {
        super(scene, objectName);
        float[] zeroSnap = new float[]{0};

        count = createSlider("Count", 0, 100, null, 0, new ChangeListener() {
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
                        //scene.removeObjects(removed.getCloudObjects());
                        scene.removeCloud(removed);
                    }
                }
            }
        });

        velocitySlider = createSlider("Velocity", -30, 30, zeroSnap, 1, 0, new ChangeListener() {
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

    public void setClouds(Array<Cloud> clouds) {
        this.clouds = new Array<>();
        this.clouds.addAll(clouds);
        count.setValue(this.clouds.size);
        velocitySlider.setValue(this.clouds.first().getAngularVelocity());
    }

    @Override
    public void deleteObjects() {
        int size = clouds.size;
        for(int i = 0; i < size; i++) {
            //Array<Orbiter> cloudObjects = clouds.pop().getCloudObjects();
            //scene.removeObjects(cloudObjects);

            Cloud cloud = clouds.pop();
            scene.removeCloud(cloud);
        }
        clouds.clear();
    }
}
