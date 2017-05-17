package zk.planet_generator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Zach on 5/16/2017.
 */
public abstract class SpaceObject implements Comparable<SpaceObject> {
    private final Sprite sprite;
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

    @Override
    public int compareTo(SpaceObject other) {
        return zCoord - other.zCoord;
    }
}
