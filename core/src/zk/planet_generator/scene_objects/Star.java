package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Zach on 5/17/2017.
 */
public class Star extends SpaceObject {
    public Star(Sprite sprite) {
        super(sprite);
        setZCoord(-1000);
    }

    @Override
    public void update(float delta) {

    }
}