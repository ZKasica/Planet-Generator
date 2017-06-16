package zk.planet_generator.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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

        VisTextButton showEditorButton = new VisTextButton("Show Editor");
        showEditorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showEditorClicked();
            }
        });
        topButtonBar.top().left().add(showEditorButton).pad(7);

        Table bottomButtonBar = new Table();
        bottomButtonBar.setFillParent(true);

        stage.addActor(topButtonBar);
    }

    private void showEditorClicked() {
        hide();
        game.getEditorUI().show();
        scene.focusOnUI();
    }
}
