package zk.planet_generator;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Zach on 5/17/2017.
 */
public class ColorGroup {
    private Array<Integer> colors;

    public ColorGroup() {
        colors = new Array<Integer>();
    }

    public ColorGroup add(int color) {
        colors.add(color);
        return this;
    }

    public int random() {
        return colors.random();
    }
}
