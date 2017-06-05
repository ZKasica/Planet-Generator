package zk.planet_generator;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.VisUI;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.omg.CORBA.ORB;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.ui.EditorUI;
import zk.planet_generator.ui.SceneUI;

import javax.swing.*;

/**
 * Planet shader code: https://gamedev.stackexchange.com/questions/9346/2d-shader-to-draw-representation-of-rotating-sphere
 * Noise generator code: http://devmag.org.za/2009/04/25/perlin-noise/
 */
public class PlanetGeneratorGame extends ApplicationAdapter {
    private Scene scene;
    private EditorUI editorUI;
    private SceneUI sceneUI;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        VisUI.load(VisUI.SkinScale.X2);

        //scene = new Scene();
        loadSceneFromJson();
        editorUI = new EditorUI(this);
        sceneUI = new SceneUI(this);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(scene);
        inputMultiplexer.addProcessor(editorUI.getStage());
        inputMultiplexer.addProcessor(sceneUI.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);

        sceneUI.hide();
    }

    private void loadSceneFromJson() {
        Json json = new Json();
        //String sceneJson = json.toJson(scene);
        //System.out.println(json.prettyPrint(sceneJson));

        String sceneJson = "{\n" +
                "Rings: [\n" +
                "\t{\n" +
                "\t\tclass: Ring\n" +
                "\t\tMinimumRadius: 77\n" +
                "\t\tMaximumRadius: 112\n" +
                "\t\tAngularVelocity: 25\n" +
                "\t\tZTilt: -24\n" +
                "\t\tXTilt: -12\n" +
                "\t\tObjectCount: 273\n" +
                "\t\tColorGroup: {\n" +
                "\t\t\tcolors: [ -538331905, -1436915713 ]\n" +
                "\t\t}\n" +
                "\t}\n" +
                "]\n" +
                "}";
        scene = json.fromJson(Scene.class, sceneJson);
    }


    @Override
    public void render() {
        super.render();

        float delta = Gdx.graphics.getDeltaTime();

        scene.update(delta);
        editorUI.render(delta);
        sceneUI.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        scene.dispose();
        editorUI.dispose();
        sceneUI.dispose();
        VisUI.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        editorUI.resize(width, height);
        sceneUI.resize(width, height);
    }

    public Scene getScene() {
        return scene;
    }

    public EditorUI getEditorUI() {
        return editorUI;
    }

    public SceneUI getSceneUI() {
        return sceneUI;
    }

    public void addProcessor(InputProcessor inputProcessor) {
        inputMultiplexer.addProcessor(inputProcessor);
    }

    public void removeProcessor(InputProcessor inputProcessor) {
        inputMultiplexer.removeProcessor(inputProcessor);
    }
}