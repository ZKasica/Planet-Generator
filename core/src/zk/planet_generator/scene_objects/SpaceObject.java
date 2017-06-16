package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public abstract class SpaceObject implements Comparable<SpaceObject>, Json.Serializable {
    private Sprite sprite;
    private int zPos;
    private int size;
    private int color;
    private Color drawColor;

    protected SpaceObject() {

    }

    public SpaceObject(Sprite sprite) {
        this(sprite, Color.rgba8888(Color.WHITE));
    }

    public SpaceObject(Sprite sprite, int color) {
        this.sprite = sprite;
        this.color = color;

        drawColor = new Color(color);

        if(sprite != null) {
            this.size = (int)sprite.getWidth();
        }
    }

    public abstract void update(float delta);

    public void render(SpriteBatch batch) {
        sprite.setColor(drawColor);
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

    public int getSize() {
        return size;
    }

    public void setColor(int color) {
        this.color = color;
        this.drawColor = new Color(color);
    }

    public int getColor() {
        return color;
    }

    public Color getDrawColor() {
        return drawColor;
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
        json.writeValue("color", color);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        size = json.readValue("size", Integer.class, jsonData);
        color = json.readValue("color", Integer.class, jsonData);
        drawColor = new Color(color);
    }
}