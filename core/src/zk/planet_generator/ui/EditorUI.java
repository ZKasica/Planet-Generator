package zk.planet_generator.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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

        // Save Scene Button
        Table testTable = new Table();
        testTable.setFillParent(true);

        VisTextButton saveButton = new VisTextButton("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveClicked();
            }
        });
        testTable.top().left().add(saveButton).pad(7);

        // Load Scene Button
        VisTextButton loadButton = new VisTextButton("Load");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadClicked();
            }
        });
        testTable.add(loadButton).pad(7);

        // Reset Button
        VisTextButton createResetButton = new VisTextButton("Reset");
        createResetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetClicked();
            }
        });
        testTable.add(createResetButton).pad(7);

        resetDialog = new VisDialog("Reset") {
            @Override
            protected void result(Object object) {
                if(object.equals("yes")) {
                    resetScene();
                    scene.createEmptyScene();
                }
            }
        };
        resetDialog.text("Do you want to reset the scene? Your changes will NOT be saved!");
        resetDialog.button("Yes", "yes");
        resetDialog.button("No", "no");

        // Random Button
        VisTextButton randomButton = new VisTextButton("Random");
        randomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                randomClicked();
            }
        });
        testTable.add(randomButton).pad(7);

        // Exit Button
        VisTextButton exitButton = new VisTextButton("Close Editor");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitClicked();
            }
        });
        testTable.add(exitButton).pad(7);
        stage.addActor(testTable);

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);

        //buttonTable.debug();

        // Create Stars Button
        VisTextButton createStarsButton = new VisTextButton("Create Stars");
        createStarsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createStarsClicked();
            }
        });
        buttonTable.bottom().left().add(createStarsButton).pad(7);

        // Create Ring Button
        VisTextButton createRingButton = new VisTextButton("Create Ring");
        createRingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRingClicked();
            }
        });
        buttonTable.add(createRingButton).pad(7);

        // Create Moon Button
        VisTextButton createMoonButton = new VisTextButton("Create Moon");
        createMoonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               createMoonClicked();
            }
        });
        buttonTable.add(createMoonButton).pad(7);

        // Create Moon Button
        VisTextButton createCloudButton = new VisTextButton("Create Clouds");
        createCloudButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createCloudClicked();
            }
        });
        buttonTable.add(createCloudButton).pad(7);

        stage.addActor(buttonTable);

        objectEditorTable = new Table();
        VisScrollPane objectEditor = new VisScrollPane(objectEditorTable);
        objectEditor.setScrollingDisabled(true, false);

        VisWindow editorWindow = new VisWindow("Object Editor", true);
        editorWindow.setSize(600, stage.getHeight());
        editorWindow.setPosition(stage.getWidth() - editorWindow.getWidth(), 0);
        editorWindow.add(objectEditor).fill().expand();
        editorWindow.setMovable(false);

        stage.addActor(editorWindow);
    }

    private void addObjectEditor(ObjectEditor objectEditor) {
        objectEditorTable.add(objectEditor).expand().fill().row();
        objectEditors.add(objectEditor);
    }

    private void saveClicked() {

    }

    private void loadClicked() {

    }

    private void createStarsClicked() {
        if(starEditor == null || starEditor.hasDeleteBeenPressed()) {
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
        if(cloudEditor == null || cloudEditor.hasDeleteBeenPressed()) {
            cloudEditor = new CloudEditor(scene, "Clouds");
            addObjectEditor(cloudEditor);
        } else {
            cloudEditor.deleteObjects();
        }

        Array<Cloud> clouds = scene.getObjectGenerator().createClouds();
        cloudEditor.setClouds(clouds);
    }

    private void resetClicked() {
        resetDialog.show(stage);
    }

    private void randomClicked() {
        resetScene();
        scene.generateObjects();
    }

    private void exitClicked() {

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

        scene.reset();
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

    public void updateToMatchScene() {

    }
}
