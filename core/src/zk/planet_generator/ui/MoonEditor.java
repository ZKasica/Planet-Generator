package zk.planet_generator.ui;

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

        // Radius slider
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
    }

    @Override
    public void deleteObjects() {
        moon.getSprite().getTexture().dispose();
        scene.removeObject(moon);
    }
}
