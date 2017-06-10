package zk.planet_generator.scene_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import zk.planet_generator.Scene;

public class Planet extends SpaceObject implements Disposable {
    private ShaderProgram planetShader;
    private float time;
    private float rotationSpeed;
    private float radius;
    private float direction;
    private Pixmap pixmap;
    private String texture;

    private Planet() {
        this(new Sprite(), null, 0);
    }

    public Planet(Sprite sprite, Pixmap pixmap, int direction) {
        super(sprite);
        this.pixmap = pixmap;
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

    public String getTextureString() {
        return texture;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void dispose() {
        planetShader.dispose();
        pixmap.dispose();
    }

    @Override
    public void write(Json json) {
        super.write(json);
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < pixmap.getWidth(); x++) {
            for(int y = 0; y < pixmap.getHeight(); y++) {
                int color = pixmap.getPixel(x, y);

                if(color == Color.rgba8888(47f / 255f, 86f / 255f, 118f / 255f, 1f)) {
                    sb.append("d");
                } else if(color == Color.rgba8888(62f / 255f, 120f / 255f, 160f / 255f, 1f)) {
                    sb.append("o");
                } else if(color == Color.rgba8888(146f / 255f, 209f / 255f, 135f / 255f, 1f)) {
                    sb.append("l");
                }
            }
        }
        json.writeValue("texture", sb.toString());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        texture = json.readValue("texture", String.class, jsonData);
        getSprite().setSize(getSize(), getSize());
        radius = getSprite().getWidth()/2;
    }
}
