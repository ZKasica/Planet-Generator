package zk.planet_generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisSlider;

/**
 * Created by zach on 5/26/17.
 */
public class EditorUI {
    private Scene scene;
    private Stage stage;

    public EditorUI(Scene scene) {
        this.scene = scene;
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        // TODO: Use input multiplexer for input

        initialize();
    }

    private void initialize() {
        VisUI.load();

        VisSlider visSlider = new VisSlider(0, 90, 1, false);
        visSlider.setWidth(200);
        stage.addActor(visSlider);
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
}
