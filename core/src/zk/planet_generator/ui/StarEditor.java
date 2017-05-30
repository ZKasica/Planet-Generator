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

        slider = createSlider("", 0, 200, null, 0, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!shouldFireChange) {
                    return;
                }

                changeStarAmount((int) (slider.getValue() - previousValue));
                previousValue = (int) slider.getValue();
            }
        });
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
        stars.clear();
    }

    @Override
    public void randomize() {

    }
}
