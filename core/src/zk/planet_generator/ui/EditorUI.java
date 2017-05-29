package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private Ring previousRing;

    private VisTable objectEditorTable;

    public EditorUI(Scene scene) {
        this.scene = scene;
        stage = new Stage();

        initialize();
    }

    private void initialize() {
        VisUI.load(VisUI.SkinScale.X2);

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);

        // Create Stars Button
        VisTextButton createStarsButton = new VisTextButton("Create Stars");
        createStarsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createStarsClicked();
            }
        });
        buttonTable.top().left().add(createStarsButton).pad(5);

        // Create Ring Button
        VisTextButton createRingButton = new VisTextButton("Create Ring");
        createRingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRingClicked();
            }
        });
        buttonTable.add(createRingButton).pad(5);

        // Create Moon Button
        VisTextButton createMoonButton = new VisTextButton("Create Moon");
        createMoonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               createMoonClicked();
            }
        });
        buttonTable.add(createMoonButton).pad(5);

        stage.addActor(buttonTable);

        // Object Editor Scrollpane
        objectEditorTable = new VisTable();
        objectEditorTable.addSeparator();

        VisScrollPane objectEditor = new VisScrollPane(objectEditorTable);
        objectEditor.setScrollingDisabled(true, false);
        objectEditor.setSize(500, stage.getHeight());
        objectEditor.setPosition(stage.getWidth() - objectEditor.getWidth(), 0);

        stage.addActor(objectEditor);

//        Code for putting the object scrollpane in a window
//        VisWindow editorWindow = new VisWindow("Object Editor", true);
//        editorWindow.setWidth(400);
//        editorWindow.setSize(400, stage.getHeight());

//        editorWindow.add(objectEditor).fill().expand();
    }

    private void addObjectEditor(ObjectEditor objectEditor) {
        objectEditorTable.add(objectEditor).fill().row();
        objectEditorTable.addSeparator();
    }

    private void createStarsClicked() {
        if(starEditor == null) {
            starEditor = new StarEditor(scene, "Stars");
            addObjectEditor(starEditor);
        } else {
            starEditor.deleteObjects();
        }

        Array<Star> stars = scene.getObjectGenerator().createStars();
        starEditor.setStars(stars);
    }

    private void createRingClicked() {
        Ring ring = scene.getObjectGenerator().createRing(previousRing);
        RingEditor ringEditor = new RingEditor(scene, "Ring", ring);
        addObjectEditor(ringEditor);
        previousRing = ring;
    }

    private void createMoonClicked() {
        Orbiter moon = scene.getObjectGenerator().createMoon();
        MoonEditor moonEditor = new MoonEditor(scene, "Moon", moon);
        addObjectEditor(moonEditor);
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
