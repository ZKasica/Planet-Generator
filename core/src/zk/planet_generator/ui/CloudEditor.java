package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Cloud;

public class CloudEditor extends ObjectEditor {
    private Array<Cloud> clouds;
    private VisSlider count;
    private VisSlider velocitySlider;
    private Color color;

    private float velocity;

    public CloudEditor(Scene scene, String objectName) {
        super(scene, objectName);
        float[] zeroSnap = new float[]{0};

        color = new Color(245f / 255f, 245f / 255f, 213f / 255f, 1f);

        count = createSlider("Count", 0, 100, null, 0, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int count = (int) ((VisSlider) actor).getValue();

                if(count > clouds.size) {
                    int amountToAdd = count - clouds.size;
                    for(int i = 0; i < amountToAdd; i++) {
                        Cloud cloud = scene.getObjectGenerator().createCloud(velocity, color);
                        clouds.add(cloud);
                    }
                } else {
                    int amountToRemove = clouds.size - count;
                    for(int i = 0; i < amountToRemove; i++) {
                        Cloud removed = clouds.pop();
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

        createColorButton("Edit Color", color, new ColorPickerAdapter() {
            @Override
            public void changed(Color newColor) {
                color = newColor;
                for(Cloud cloud : clouds) {
                    cloud.setColor(Color.rgba8888(color));
                }
            }

            @Override
            public void finished(Color newColor) {
                color = newColor;
                for(Cloud cloud : clouds) {
                    cloud.setColor(Color.rgba8888(color));
                }
            }
        });

        addBottomBar();
    }

    public void setClouds(Array<Cloud> clouds) {
        this.clouds = new Array<>(clouds);
        count.setValue(this.clouds.size);

        if(this.clouds.size > 0) {
            velocitySlider.setValue(this.clouds.first().getAngularVelocity());
        } else {
            velocitySlider.setValue(0);
        }
    }

    @Override
    public void deleteObjects() {
        int size = clouds.size;
        for(int i = 0; i < size; i++) {
            Cloud cloud = clouds.pop();
            scene.removeCloud(cloud);
        }
        clouds.clear();
    }

    @Override
    public void hideInfo() {

    }
}
