package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import zk.planet_generator.Scene;

/**
 * Created by zach on 5/27/17.
 */
public abstract class ObjectEditor extends Table {
    protected Scene scene;
    private String objectName;

    public ObjectEditor(Scene scene, String objectName) {
        this.scene = scene;
        left().add(new VisLabel(objectName)).row();
    }

    public abstract void deleteObjects();
}
