package zk.planet_generator.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;
import zk.planet_generator.PlanetGeneratorGame;
import zk.planet_generator.scene_objects.Cloud;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.scene_objects.Star;

public class EditorUI extends GameUI {
    private Array<ObjectEditor> objectEditors;
    private StarEditor starEditor;
    private CloudEditor cloudEditor;
    private Ring previousRing; // TODO: Handle previousRing when the outer ring is deleted

    private VisDialog resetDialog;
    private Table objectEditorTable;

    private FileChooser saveFileChooser;
    private FileChooser loadFileChooser;

    public EditorUI(PlanetGeneratorGame game) {
        super(game);
    }

    @Override
    protected void initialize() {
        objectEditors = new Array<>();

        // Save Scene Button
        Table topButtonBar = new Table();
        topButtonBar.setFillParent(true);

        VisTextButton saveButton = new VisTextButton("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveClicked();
            }
        });
        topButtonBar.top().left().add(saveButton).pad(7);

        // Load Scene Button
        VisTextButton loadButton = new VisTextButton("Load");
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadClicked();
            }
        });
        topButtonBar.add(loadButton).pad(7);

        // Reset Button
        VisTextButton createResetButton = new VisTextButton("Clear");
        createResetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetClicked();
            }
        });
        topButtonBar.add(createResetButton).pad(7);

        resetDialog = new VisDialog("Reset") {
            @Override
            protected void result(Object object) {
                if(object.equals("yes")) {
                    resetScene();
                    scene.createEmptyScene();
                }
            }
        };
        resetDialog.text("Do you want to clear the scene? Your changes will NOT be saved!");
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
        topButtonBar.add(randomButton).pad(7);

        // Exit Button
        VisTextButton exitButton = new VisTextButton("Close Editor");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitClicked();
            }
        });
        topButtonBar.add(exitButton).pad(7);
        stage.addActor(topButtonBar);

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

        FileChooser.setDefaultPrefsName("planet_generator_prefs");

        FileTypeFilter planetFileFilter = new FileTypeFilter(false);
        planetFileFilter.addRule("Planet File (*.plt)", "plt");

        saveFileChooser = new FileChooser(FileChooser.Mode.SAVE);
        saveFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        saveFileChooser.setSize(1000, 600);
        saveFileChooser.setFileTypeFilter(planetFileFilter);
        saveFileChooser.setListener(new SingleFileChooserListener() {
            @Override
            protected void selected(FileHandle file) {
                game.saveScene(file.path());
            }
        });

        loadFileChooser = new FileChooser(FileChooser.Mode.OPEN);
        loadFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        loadFileChooser.setSize(1000, 600);
        loadFileChooser.setFileTypeFilter(planetFileFilter);
        loadFileChooser.setListener(new SingleFileChooserListener() {
            @Override
            public void selected(FileHandle file) {
                clearEditors();
                game.loadScene(file.path());
                updateScene();
                updateToMatchScene();
            }
        });
    }

    private void addObjectEditor(ObjectEditor objectEditor) {
        objectEditorTable.add(objectEditor).expand().fill().row();
        objectEditors.add(objectEditor);
    }

    private void saveClicked() {
        getStage().addActor(saveFileChooser.fadeIn());
    }

    private void loadClicked() {
        getStage().addActor(loadFileChooser.fadeIn());
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

        Array<Cloud> clouds = scene.getObjectGenerator().createClouds(Color.rgba8888(245f / 255f, 245f / 255f, 213f / 255f, 1f));
        cloudEditor.setClouds(clouds);
    }

    private void resetClicked() {
        resetDialog.show(stage);
    }

    private void randomClicked() {
        resetScene();
        scene.generateObjects();
        updateToMatchScene();
    }

    private void exitClicked() {
        hide();
        game.getSceneUI().show();
        scene.focusOnPlanet();

        for(ObjectEditor editor : objectEditors) {
            editor.hideInfo();
        }
    }

    private void clearEditors() {
        int objectEditorCount = objectEditors.size;
        for(int i = 0; i < objectEditorCount; i++) {
            ObjectEditor editor = objectEditors.pop();
            editor.delete();
        }
    }

    private void resetScene() {
        clearEditors();

        starEditor = null;
        cloudEditor = null;
        previousRing = null;

        scene.clear();
    }

    public void updateToMatchScene() {
        starEditor = new StarEditor(scene, "Stars");
        starEditor.setStars(scene.getStars());
        addObjectEditor(starEditor);

        cloudEditor = new CloudEditor(scene, "Clouds");
        cloudEditor.setClouds(scene.getClouds());
        addObjectEditor(cloudEditor);

        int ringId = 0;
        for(Ring ring : scene.getRings()) {
            addObjectEditor(new RingEditor(scene, "Ring " + (++ringId), ring));
        }

        int moonId = 0;
        for(Orbiter moon : scene.getMoons()) {
            addObjectEditor(new MoonEditor(scene, "Moon " + (++moonId), moon));
        }
    }
}
