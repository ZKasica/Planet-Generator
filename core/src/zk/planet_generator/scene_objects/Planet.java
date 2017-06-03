package zk.planet_generator.scene_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import zk.planet_generator.Scene;

public class Planet extends SpaceObject implements Disposable {
    private ShaderProgram planetShader;
    private float time;
    private float rotationSpeed;
    private float radius;
    private float direction;

    public Planet(Sprite sprite, int direction) {
        super(sprite);
        this.direction = direction;

        rotationSpeed = 1/50f;
        radius = sprite.getWidth()/2;

        planetShader = new ShaderProgram(Gdx.files.internal("shaders/planet.vsh"), Gdx.files.internal("shaders/planet.fsh"));
        if(!planetShader.isCompiled()) {
            Gdx.app.error("Planet Shader Error", "\n" + planetShader.getLog());
        }
    }

    @Override
    public void update(float delta) {
        time += rotationSpeed * delta;

        // If the code below this comment is moved to update, it causes graphic issues with orbiting objects
        planetShader.begin();
        planetShader.setUniformf("time", direction * time);
        planetShader.end();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setShader(planetShader);
        super.render(batch);
        batch.setShader(null);
    }

    public float getWidthAtY(float y) {
        return (float) Math.sqrt(radius*radius - y*y);
    }

    public float getMinimumCloudRadiusAtY(float y) {
        return getWidthAtY(y) + 6;
    }

    @Override
    public void dispose() {
        planetShader.dispose();
    }
}
