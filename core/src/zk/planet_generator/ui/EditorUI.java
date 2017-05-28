package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.scene_objects.Star;

/**
 * Created by zach on 5/26/17.
 */
public class EditorUI {
    private Scene scene;
    private Stage stage;

    private StarEditor starEditor;

    private VisTable objectEditorTable;

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

        Table table = new Table();
        table.setFillParent(true);
        //table.debug();

        VisTextButton createStarsButton = new VisTextButton("Create Stars");
        createStarsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(starEditor == null) {
                    starEditor = new StarEditor(scene, "Stars");
                    addObjectEditor(starEditor);
                } else {
                    starEditor.deleteObjects();
                }

                Array<Star> stars = scene.getObjectGenerator().createStars();
                starEditor.setStars(stars);
            }
        });
        table.top().left().add(createStarsButton).pad(5);

        VisTextButton createRingButton = new VisTextButton("Create Ring");
        createRingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scene.getObjectGenerator().createRing(null);
            }
        });
        table.add(createRingButton).pad(5);

        VisTextButton createMoonButton = new VisTextButton("Create Moon");
        createMoonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Orbiter moon = scene.getObjectGenerator().createMoon();
                MoonEditor moonEditor = new MoonEditor(scene, "Moon", moon);
                addObjectEditor(moonEditor);
            }
        });
        table.add(createMoonButton).pad(5);

        stage.addActor(table);

        objectEditorTable = new VisTable();
        objectEditorTable.addSeparator();

        VisScrollPane objectEditor = new VisScrollPane(objectEditorTable);
        objectEditor.setScrollingDisabled(true, false);
        objectEditor.setSize(500, stage.getHeight());
        objectEditor.setPosition(stage.getWidth() - objectEditor.getWidth(), 0);

        stage.addActor(objectEditor);

//        VisWindow editorWindow = new VisWindow("Object Editor", true);
//        editorWindow.setWidth(400);
//        editorWindow.setSize(400, stage.getHeight());

//        editorWindow.add(objectEditor).fill().expand();
    }

    private void addObjectEditor(ObjectEditor objectEditor) {
        objectEditorTable.add(objectEditor).fill().row();
        objectEditorTable.addSeparator();
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
