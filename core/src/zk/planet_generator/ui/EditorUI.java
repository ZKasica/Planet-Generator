package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Cloud;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.scene_objects.Star;

/**
 * Created by zach on 5/26/17.
 */
public class EditorUI {
    private Scene scene;
    private Stage stage;

    private Array<ObjectEditor> objectEditors;
    private StarEditor starEditor;
    private CloudEditor cloudEditor;
    private Ring previousRing; // TODO: Handle previousRing when the outer ring is deleted

    private VisDialog resetDialog;

    private Table objectEditorTable;

    public EditorUI(Scene scene) {
        this.scene = scene;
        stage = new Stage();

        initialize();
    }

    private void initialize() {
        VisUI.load(VisUI.SkinScale.X2);

        objectEditors = new Array<>();

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

        // Create Moon Button
        VisTextButton createCloudButton = new VisTextButton("Create Clouds");
        createCloudButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createCloudClicked();
            }
        });
        buttonTable.add(createCloudButton).pad(5);

        // Reset Button
        VisTextButton createResetButton = new VisTextButton("Reset");
        createResetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetClicked();
            }
        });
        buttonTable.add(createResetButton).pad(5);

        resetDialog = new VisDialog("Reset") {
            @Override
            protected void result(Object object) {
                if(object.equals("yes")) {
                    resetScene();
                }
            }
        };
        resetDialog.text("Do you want to reset the scene? Your changes will NOT be saved!");
        resetDialog.button("Yes", "yes");
        resetDialog.button("No", "no");

        stage.addActor(buttonTable);

        objectEditorTable = new Table();
        VisScrollPane objectEditor = new VisScrollPane(objectEditorTable);
        objectEditor.setScrollingDisabled(true, false);

        VisWindow editorWindow = new VisWindow("Object Editor", true);
        editorWindow.setSize(600, stage.getHeight());
        editorWindow.setPosition(stage.getWidth() - editorWindow.getWidth(), 0);
        editorWindow.add(objectEditor).fill().expand();

        stage.addActor(editorWindow);
    }

    private void addObjectEditor(ObjectEditor objectEditor) {
        objectEditorTable.add(objectEditor).expand().fill().row();
        objectEditors.add(objectEditor);
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

    private void createCloudClicked() {
        if(cloudEditor == null) {
            Array<Cloud> clouds = scene.getObjectGenerator().createClouds();
            cloudEditor = new CloudEditor(scene, "Clouds", clouds);
            addObjectEditor(cloudEditor);
        }
    }

    private void resetClicked() {
        resetDialog.show(stage);
    }

    private void resetScene() {
        int objectEditorCount = objectEditors.size;
        for(int i = 0; i < objectEditorCount; i++) {
            ObjectEditor editor = objectEditors.pop();
            editor.delete();
        }

        starEditor = null;
        cloudEditor = null;
        previousRing = null;
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
