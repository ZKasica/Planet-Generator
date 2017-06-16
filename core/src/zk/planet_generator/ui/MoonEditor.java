package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Trajectory;

public class MoonEditor extends ObjectEditor {
    private Orbiter moon;
    private boolean showTrajectory;
    private Trajectory trajectory;
    private VisTextButton trajectoryButton;

    public MoonEditor(Scene scene, String objectName, Orbiter moon) {
        super(scene, objectName);
        this.moon = moon;

        float[] zeroSnap = new float[]{0};

        trajectoryButton = new VisTextButton("Show Trajectory");
        trajectoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTrajectory = !showTrajectory;
                if(showTrajectory) {
                    trajectoryButton.setText("Hide Trajectory");
                    trajectory = scene.addTrajectory(moon);
                } else {
                    trajectoryButton.setText("Show Trajectory");
                    scene.removeTrajectory(trajectory);
                }
            }
        });
        getTopBar().add(trajectoryButton).pad(0, 20, 0, 20);

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

        createSlider("Size", 5, 32, null, moon.getSpriteSize(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Sprite sprite = scene.getObjectGenerator().createMoonSprite((int) ((VisSlider) actor).getValue());
                moon.setSprite(sprite);
                moon.setColor(moon.getColor());
            }
        });

        createColorButton("Edit Color", moon.getDrawColor(), new ColorPickerAdapter() {
            @Override
            public void changed(Color newColor) {
                moon.setColor(Color.rgba8888(newColor));
            }

            @Override
            public void finished(Color newColor) {
                moon.setColor(Color.rgba8888(newColor));
            }
        });

        addBottomBar();
    }

    @Override
    public void deleteObjects() {
        scene.removeMoon(moon);

        hideInfo();
    }

    @Override
    public void hideInfo() {
        if(trajectory != null) {
            scene.removeTrajectory(trajectory);
        }
        trajectoryButton.setText("Show Trajectory");
        showTrajectory = false;
    }
}
