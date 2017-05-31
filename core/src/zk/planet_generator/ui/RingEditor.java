package zk.planet_generator.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisSlider;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;

/**
 * Created by zach on 5/28/17.
 */
public class RingEditor extends ObjectEditor {
    private Ring ring;

    private VisSlider minRadiusSlider;
    private VisSlider maxRadiusSlider;
    private boolean shouldFireChange;

    public RingEditor(Scene scene, String objectName, Ring ring) {
        super(scene, objectName);
        this.ring = ring;

        float[] zeroSnap = new float[]{0};
        shouldFireChange = true;

        createSlider("Velocity", -100, 100, zeroSnap, ring.getAngularVelocity(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ring.setAngularVelocity(((VisSlider) actor).getValue());
            }
        });

        createSlider("Z-Rot", -90, 90, zeroSnap, ring.getZTilt(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ring.setZTilt(((VisSlider) actor).getValue());
            }
        });

        createSlider("X-Rot", -90, 90, zeroSnap, ring.getXTilt(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ring.setXTilt(((VisSlider) actor).getValue());
            }
        });

        float radiusMin = scene.getPlanet().getMinimumCloudRadiusAtY(0) + 5;
        minRadiusSlider = createSlider("Min. Radius", radiusMin, 400, zeroSnap, ring.getMinRadius(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(shouldFireChange) {
                    ring.setMinimumRadius(((VisSlider) actor).getValue());
                    shouldFireChange = false;
                    maxRadiusSlider.setValue(ring.getMaxRadius());
                    shouldFireChange = true;
                }
            }
        });

        maxRadiusSlider = createSlider("Max. Radius", radiusMin, 400, zeroSnap, ring.getMaxRadius(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(shouldFireChange) {
                    ring.setMaximumRadius(((VisSlider) actor).getValue());
                    shouldFireChange = false;
                    minRadiusSlider.setValue(ring.getMinRadius());
                    shouldFireChange = true;
                }
            }
        });

        createSlider("Objects", 25, 500, null, ring.getObjectCount(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int count = (int) ((VisSlider) actor).getValue();

                if(count > ring.getObjectCount()) {
                    for(int i = 0; i < count - ring.getObjectCount(); i++) {
                        scene.getObjectGenerator().createObjectInRing(ring);
                    }
                } else {
                    for(int i = 0; i < ring.getObjectCount() - count; i++) {
                        Orbiter removed = ring.removeObject();
                        scene.removeObject(removed);
                    }
                }
            }
        });
    }

    @Override
    public void deleteObjects() {
        scene.removeObjects(ring.getObjects());
    }
}
