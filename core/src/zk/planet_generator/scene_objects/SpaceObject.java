package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class SpaceObject implements Comparable<SpaceObject> {
    private Sprite sprite;
    private int zPos;

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
}