package zk.planet_generator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Zach on 5/16/2017.
 */
public class Planet extends SpaceObject {
    private ShaderProgram planetShader;

    public Planet(Sprite sprite, ShaderProgram planetShader) {
        super(sprite);
        this.planetShader = planetShader;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setShader(planetShader);
        super.render(batch);
        batch.setShader(null);
    }
}
