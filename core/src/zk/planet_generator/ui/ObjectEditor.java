package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import zk.planet_generator.Scene;

/**
 * Created by zach on 5/27/17.
 */
public abstract class ObjectEditor extends Table {
    protected Scene scene;
    private Table top;
    private boolean hasDeleteBeenPressed;

    public ObjectEditor(Scene scene, String objectName) {
        this.scene = scene;

        top = new Table();
        top.add(new VisLabel(objectName)).expandX().pad(0, 20, 0, 20);

        VisTextButton deleteObject = new VisTextButton("Delete");
        deleteObject.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hasDeleteBeenPressed = true;
                delete();
            }
        });
        top.add(deleteObject).pad(0, 20, 0, 20);

        add(top).padTop(80).colspan(4).row();
    }

    public abstract void deleteObjects();
    public abstract void hideInfo();

    public void delete() {
        deleteObjects();
        this.remove();
    }

    public VisSlider createSlider(String label, float minimum, float maximum, float[] snapValues, float initialValue, ChangeListener changeListener) {
        return createSlider(label, minimum, maximum, snapValues, 10, initialValue, changeListener);
    }

    public VisSlider createSlider(String label, float minimum, float maximum, float[] snapValues, float threshold, float initialValue, ChangeListener changeListener) {
        if(!label.isEmpty()) {
            add(new VisLabel(label)).left().padRight(10).padTop(20);
        }
        add(new VisLabel((int)minimum + "")).padRight(5).padTop(20);

        VisSlider slider = new VisSlider(minimum, maximum, 1, false);
        slider.setSnapToValues(snapValues, threshold);
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
        add(new VisLabel("" + (int)maximum)).padTop(20).row();

        return slider;
    }

    public boolean hasDeleteBeenPressed() {
        return hasDeleteBeenPressed;
    }

    public Table getTopBar() {
        return top;
    }
}
