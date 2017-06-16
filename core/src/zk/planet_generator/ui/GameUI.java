package zk.planet_generator.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import zk.planet_generator.PlanetGeneratorGame;
import zk.planet_generator.Scene;

public abstract class GameUI {
    public static final float FADE_DURATION = 0.25f;

    protected PlanetGeneratorGame game;
    protected Scene scene;
    protected Stage stage;

    public GameUI(PlanetGeneratorGame game) {
        this.game = game;
        this.scene = game.getScene();
        stage = new Stage();

        initialize();
    }

    protected abstract void initialize();

    public void show() {
        stage.addAction(Actions.fadeIn(FADE_DURATION, Interpolation.circleIn));
        game.addProcessor(stage);
    }

    public void hide() {
        stage.addAction(Actions.fadeOut(FADE_DURATION, Interpolation.circleOut));
        game.removeProcessor(stage);
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
    }

    public Stage getStage() {
        return stage;
    }

    protected void updateScene() {
        scene = game.getScene();
    }
}
