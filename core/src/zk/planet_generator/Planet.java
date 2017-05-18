package zk.planet_generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Zach on 5/16/2017.
 */
public class Planet extends SpaceObject {
    private float time;
    private float speed = 1/50f;

    public Planet(Sprite sprite) {
        super(sprite);
    }

    @Override
    public void update(float delta) {
        time += speed * delta;
        PlanetGenerator.planetShader.begin();
        PlanetGenerator.planetShader.setUniformf("time", time);
        PlanetGenerator.planetShader.end();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setShader(PlanetGenerator.planetShader);
        super.render(batch);
        batch.setShader(null);
    }
}
