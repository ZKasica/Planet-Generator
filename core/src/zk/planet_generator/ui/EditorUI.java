package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;
import zk.planet_generator.Scene;

/**
 * Created by zach on 5/26/17.
 */
public class EditorUI {
    private Scene scene;
    private Stage stage;

    public EditorUI(Scene scene) {
        this.scene = scene;
        stage = new Stage();

        initialize();
    }

    private void initialize() {
        VisUI.load(VisUI.SkinScale.X2);

//        VisSlider visSlider = new VisSlider(-200, 200, 1, false);
//        visSlider.setWidth(400);
//        visSlider.setSnapToValues(new float[]{0}, 10);
//        visSlider.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                //scene.setRingZTilt(visSlider.getValue());
//                //scene.setRingAngularVelocity(visSlider.getValue());
//                //scene.setRingXTilt(visSlider.getValue());
//            }
//        });
//        stage.addActor(visSlider);

        VisTextButton createStarsButton = new VisTextButton("Create Stars");
        createStarsButton.setPosition(0, stage.getHeight() - createStarsButton.getHeight());
        createStarsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scene.getObjectGenerator().createStars(80);
            }
        });
        stage.addActor(createStarsButton);

        VisTextButton createRingsButton = new VisTextButton("Create Ring");
        createRingsButton.setPosition(createStarsButton.getWidth(), stage.getHeight() - createRingsButton.getHeight());
        createRingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(createRingsButton);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
