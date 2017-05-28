package zk.planet_generator;

import com.badlogic.gdx.*;
import zk.planet_generator.ui.EditorUI;

/**
 * Planet shader code: https://gamedev.stackexchange.com/questions/9346/2d-shader-to-draw-representation-of-rotating-sphere
 * Noise generator code: http://devmag.org.za/2009/04/25/perlin-noise/
 */
public class PlanetGeneratorGame extends ApplicationAdapter {
    private Scene scene;
    private EditorUI editorUI;

    @Override
    public void create() {
        scene = new Scene();
        editorUI = new EditorUI(scene);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(scene);
        inputMultiplexer.addProcessor(editorUI.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        super.render();
        scene.render();
        editorUI.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();
        scene.dispose();
        editorUI.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        editorUI.resize(width, height);
    }
}