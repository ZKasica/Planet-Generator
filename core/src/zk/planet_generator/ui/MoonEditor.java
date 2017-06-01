package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;
import zk.planet_generator.Scene;
import zk.planet_generator.Trajectory;
import zk.planet_generator.scene_objects.Orbiter;

/**
 * Created by zach on 5/27/17.
 */
public class MoonEditor extends ObjectEditor {
    private Orbiter moon;
    private boolean showTrajectory;
    private Trajectory trajectory;

    public MoonEditor(Scene scene, String objectName, Orbiter moon) {
        super(scene, objectName);
        this.moon = moon;

        float[] zeroSnap = new float[]{0};

        VisTextButton test = new VisTextButton("Show Trajectory");
        test.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTrajectory = !showTrajectory;
                if(showTrajectory) {
                    test.setText("Hide Trajectory");
                    trajectory = scene.addTrajectory(moon);
                } else {
                    test.setText("Show Trajectory");
                    scene.removeTrajectory(trajectory);
                }
            }
        });
        getTopBar().add(test).pad(0, 20, 0, 20);

        createSlider("Velocity", -200, 200, zeroSnap, moon.getAngularVelocity(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setAngularVelocity(((VisSlider) actor).getValue());
            }
        });

        float radiusMin = scene.getPlanet().getMinimumCloudRadiusAtY(0) + 5;
        createSlider("Radius", radiusMin, 400, null, moon.getRadius(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setRadius(((VisSlider) actor).getValue());
            }
        });

        createSlider("Z-Rot", -90, 90, zeroSnap, moon.getZTilt(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setZTilt(((VisSlider) actor).getValue());
            }
        });

        createSlider("X-Rot", -90, 90, zeroSnap, moon.getXTilt(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setXTilt(((VisSlider) actor).getValue());
            }
        });

        createSlider("Size", 5, 32, null, moon.getSize(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Sprite sprite = scene.getObjectGenerator().createMoonSprite(moon.getColor(), (int) ((VisSlider) actor).getValue());
                moon.setSprite(sprite);
            }
        });
    }

    @Override
    public void deleteObjects() {
        scene.removeMoon(moon);

        if(trajectory != null) {
            scene.removeTrajectory(trajectory);
        }
    }
}
