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

        // Velocity Slider
        add(new VisLabel("Velocity")).left().padRight(10);
        add(new VisLabel(-200 + "")).padRight(5);

        VisSlider velocitySlider = new VisSlider(-200, 200, 1, false);
        velocitySlider.setSnapToValues(new float[]{0}, 10);
        velocitySlider.setValue(moon.getAngularVelocity());
        velocitySlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        velocitySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setAngularVelocity(velocitySlider.getValue());
            }
        });

        add(velocitySlider).expandX().fill().padRight(5);
        add(new VisLabel("" + velocitySlider.getMaxValue())).row();

        // Radius Slider
        int min = (int)scene.getPlanet().getMinimumCloudRadiusAtY(0) + 5;

        add(new VisLabel("Radius")).left();
        add(new VisLabel(min + ""));

        VisSlider radiusSlider = new VisSlider(min, 400, 1, false);
        velocitySlider.setSnapToValues(new float[]{0}, 10);
        velocitySlider.setValue(moon.getRadius());

        radiusSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        radiusSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setRadius(radiusSlider.getValue());
            }
        });

        add(radiusSlider).expandX().fill().padRight(5);
        add(new VisLabel("" + radiusSlider.getMaxValue())).row();

        // Z Rotation Slider
        add(new VisLabel("Z-Rot.")).left();
        add(new VisLabel(-90 + ""));

        VisSlider zSlider = new VisSlider(-90, 90, 1, false);
        zSlider.setSnapToValues(new float[]{0}, 10);
        zSlider.setValue(moon.getzTilt());

        zSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        zSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setZTilt(zSlider.getValue());
            }
        });

        add(zSlider).expandX().fill().padRight(5);
        add(new VisLabel("" + zSlider.getMaxValue())).row();

        // X Rotation Slider
        add(new VisLabel("X-Rot.")).left();
        add(new VisLabel(-90 + ""));

        VisSlider xSlider = new VisSlider(-90, 90, 1, false);
        xSlider.setSnapToValues(new float[]{0}, 10);
        xSlider.setValue(moon.getxTilt());

        xSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        xSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moon.setXTilt(xSlider.getValue());
            }
        });

        add(xSlider).expandX().fill().padRight(5);
        add(new VisLabel("" + xSlider.getMaxValue())).row();

        // Size Slider
        add(new VisLabel("Size")).left();
        add(new VisLabel(5 + ""));

        VisSlider sizeSlider = new VisSlider(5, 32, 1, false);
        sizeSlider.setValue(moon.getSize());

        sizeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        sizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Sprite sprite = scene.getObjectGenerator().createMoonSprite(moon.getColor(), (int)sizeSlider.getValue());
                moon.setSprite(sprite);
            }
        });

        add(sizeSlider).expandX().fill().padRight(5);
        add(new VisLabel("" + sizeSlider.getMaxValue())).row();
    }

    @Override
    public void deleteObjects() {
        moon.getSprite().getTexture().dispose();
        scene.removeObject(moon);
    }
}
