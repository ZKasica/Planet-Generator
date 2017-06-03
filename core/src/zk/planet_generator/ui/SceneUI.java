package zk.planet_generator.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import zk.planet_generator.PlanetGeneratorGame;

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
