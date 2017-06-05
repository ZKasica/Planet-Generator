package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public abstract class SpaceObject implements Comparable<SpaceObject>, Json.Serializable {
    private Sprite sprite;
    private int zPos;

    protected SpaceObject() {

    }

    public SpaceObject(Sprite sprite) {
        this.sprite = sprite;
    }

    public abstract void update(float delta);

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setZPos(int zPos) {
        this.zPos = zPos;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public int compareTo(SpaceObject other) {
        return zPos - other.zPos;
    }

    @Override
    public boolean equals(Object o) {
        SpaceObject other = (SpaceObject) o;
        return sprite.equals(other.sprite) && zPos == other.zPos;
    }

    @Override
    public void write(Json json) {
        json.writeValue("size", sprite.getWidth());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}