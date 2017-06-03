package zk.planet_generator.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import zk.planet_generator.PlanetGeneratorGame;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Cloud;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.scene_objects.Star;

/**
 * Created by zach on 6/2/17.
 */
public class SceneUI extends GameUI {
    public SceneUI(PlanetGeneratorGame game) {
        super(game);
    }

    @Override
    protected void initialize() {
        Table topButtonBar = new Table();
        topButtonBar.setFillParent(true);

        VisTextButton saveButton = new VisTextButton("Show Editor");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showEditorClicked();
            }
        });
        topButtonBar.top().left().add(saveButton).pad(7);

        stage.addActor(topButtonBar);
    }

    private void showEditorClicked() {
        hide();
        game.getEditorUI().show();
        scene.focusOnUI();
    }
}
