package zk.planet_generator;

import com.badlogic.gdx.utils.Array;

public class ColorGroup {
    private Array<Integer> colors;

    public ColorGroup() {
        colors = new Array<Integer>();
    }

    public int at(int index) {
        return colors.get(index);
    }

    public void set(int index, int newColor) {
        colors.set(index, newColor);
    }

    public ColorGroup add(int color) {
        colors.add(color);
        return this;
    }

    public int random() {
        return colors.random();
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < colors.size; i++) {
            str += "0x" + Integer.toHexString(colors.get(i));
            if(i != colors.size - 1) {
                str += ", ";
            }
        }
        return str;
    }
}
