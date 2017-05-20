package zk.planet_generator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Zach on 5/16/2017.
 */
public class Planet extends SpaceObject {
    private float time;
    private float rotationSpeed;
    private float radius;

    public Planet(Sprite sprite) {
        super(sprite);

        rotationSpeed = 1/50f;
        radius = sprite.getWidth()/2;
    }

    @Override
    public void update(float delta) {
        time += rotationSpeed * delta;

        // If the code below this comment is moved to render, it causes graphic issues with orbiting objects
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
        return (float) Math.sqrt(radius*radius - y*y);
    }

    public float getMinimumCloudRadiusAtY(float y) {
        return getWidthAtY(y) + 6;
    }
}
