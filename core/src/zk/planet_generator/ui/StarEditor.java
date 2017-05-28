package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Star;

/**
 * Created by zach on 5/27/17.
 */
public class StarEditor extends ObjectEditor {
    private Array<Star> stars;
    private VisSlider slider;
    private int previousValue;
    private boolean shouldFireChange;

    public StarEditor(Scene scene, String objectName) {
        super(scene, objectName);
        shouldFireChange = true;
        initialize();
    }

    private void initialize() {
        add(new VisLabel(0 + "")).padRight(5);

        slider = new VisSlider(0, 200, 1, false);
        slider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                event.stop();
                return false;
            }
        });

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!shouldFireChange) {
                    return;
                }

                changeStarAmount((int) (slider.getValue() - previousValue));
                previousValue = (int) slider.getValue();
            }
        });

        add(slider).expandX().fill().padRight(5);
        add(new VisLabel("  " + 200));
    }

    private void changeStarAmount(int change) {
        if(change > 0) {
            Array<Star> stars = scene.getObjectGenerator().createStars(change);
            this.stars.addAll(stars);
        } else {
            for(int i = change; i < 0; i++) {
                scene.removeObject(stars.pop());
            }
        }
    }

    public void setStars(Array<Star> stars) {
        this.stars = new Array<>();
        this.stars.addAll(stars);
        shouldFireChange = false;
        slider.setValue(this.stars.size);
        shouldFireChange = true;
        previousValue = (int)slider.getValue();
    }

    @Override
    public void deleteObjects() {
        int size = stars.size;
        for(int i = 0; i < size; i++) {
            scene.removeObject(stars.pop());
        }
    }
}
