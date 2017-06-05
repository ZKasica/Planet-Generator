package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import zk.planet_generator.Scene;

public class Star extends SpaceObject {
    private Star() {

    }

    public Star(Sprite sprite) {
        super(sprite);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("xPos", getSprite().getX());
        json.writeValue("yPos", getSprite().getY());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setSprite(new Sprite());
        int size = json.readValue("size", Integer.class, jsonData);
        getSprite().setSize(size, size);
        int x = json.readValue("xPos", Integer.class, jsonData);
        int y = json.readValue("yPos", Integer.class, jsonData);
        getSprite().setPosition(x, y);
    }
}
