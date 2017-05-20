package zk.planet_generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Zach on 5/16/2017.
 */
public class Planet extends SpaceObject {
    private float time;
    private float speed = 1/50f;
    private float radius;

    public Planet(Sprite sprite) {
        super(sprite);

        radius = sprite.getWidth()/2;
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

    public float getWidthAtY(float y) {
        if(Math.abs(y) > Math.abs(radius)) {
            return -1;
        }

        return (float) Math.sqrt(radius*radius - y*y);
    }

    public float getCloudRadiusAtY(float y) {
        return getWidthAtY(y) + 6;
    }
}
