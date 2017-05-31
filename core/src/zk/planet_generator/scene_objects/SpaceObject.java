package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Zach on 5/16/2017.
 */
public abstract class SpaceObject implements Comparable<SpaceObject> {
    private Sprite sprite;
    private int zCoord;

    public SpaceObject(Sprite sprite) {
        this.sprite = sprite;
    }

    public abstract void update(float delta);

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setZCoord(int zCoord) {
        this.zCoord = zCoord;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getZCoord() {
        return zCoord;
    }

    @Override
    public int compareTo(SpaceObject other) {
        return zCoord - other.zCoord;
    }

    @Override
    public boolean equals(Object o) {
        SpaceObject other = (SpaceObject) o;
        return sprite.equals(other.sprite) && zCoord == other.zCoord;
    }
}