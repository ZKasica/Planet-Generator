package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import zk.planet_generator.Scene;

/**
 * Created by zach on 5/27/17.
 */
public abstract class ObjectEditor extends Table {
    protected Scene scene;
    private String objectName;

    public ObjectEditor(Scene scene, String objectName) {
        this.scene = scene;
        left().add(new VisLabel(objectName)).padTop(30).row();
    }

    public abstract void deleteObjects();

    public VisSlider createSlider(String label, float minimum, float maximum, float[] snapValues, float initialValue, ChangeListener changeListener) {
        if(!label.isEmpty()) {
            add(new VisLabel(label)).left().padRight(10).padTop(20);
        }
        add(new VisLabel(minimum + "")).padRight(5).padTop(20);

        VisSlider slider = new VisSlider(minimum, maximum, 1, false);
        slider.setSnapToValues(snapValues, 10);
        slider.setValue(initialValue);
        slider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        slider.addListener(changeListener);

        add(slider).expandX().fill().padRight(5).padTop(20);
        add(new VisLabel("" + slider.getMaxValue())).padTop(20).row();

        return slider;
    }
}
