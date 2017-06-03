package zk.planet_generator;

import com.badlogic.gdx.utils.Array;

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
