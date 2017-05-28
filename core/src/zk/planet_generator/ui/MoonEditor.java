package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import org.omg.CORBA.ORB;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Orbiter;

/**
 * Created by zach on 5/27/17.
 */
public class MoonEditor extends ObjectEditor {
    private Orbiter moon;

    public MoonEditor(Scene scene, String objectName, Orbiter moon) {
        super(scene, objectName);
        this.moon = moon;

        float[] zeroSnap = new float[]{0};

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

        createSlider("Z-Rot", -90, 90, zeroSnap, moon.getzTilt(), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setZTilt(((VisSlider) actor).getValue());
            }
        });

        createSlider("X-Rot", -90, 90, zeroSnap, moon.getxTilt(), new ChangeListener() {
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
        moon.getSprite().getTexture().dispose();
        scene.removeObject(moon);
    }
}
