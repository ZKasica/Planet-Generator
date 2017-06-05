package zk.planet_generator.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.Test;
import zk.planet_generator.ColorGroup;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by zach on 6/5/17.
 */
public class SavingLoadingTests {
    @Test
    public void testRingJson() {
        Json json = new Json();

        Array<Orbiter> array = new Array<>();
        int objectCount = 10;
        for(int i = 0; i < objectCount; i++) {
            array.add(new Orbiter(null, new Orbiter.OrbiterBlueprint()));
        }

        ColorGroup colorGroup = new ColorGroup()
                .add(Color.rgba8888(1, 0, 0, 1))
                .add(Color.rgba8888(0, 0, 1, 1));

        Ring saveRing = new Ring(array, colorGroup, 10, 50);
        saveRing.setAngularVelocity(100);
        saveRing.setXTilt(25);
        saveRing.setZTilt(50);

        String ringJson = json.toJson(saveRing);
        Ring loadRing = json.fromJson(Ring.class, ringJson);

        assertNotEquals(null, loadRing);
        assertEquals(10, loadRing.getMinimumRadius());
        assertEquals(50, loadRing.getMaximumRadius());
        assertEquals(100, loadRing.getAngularVelocity());
        assertEquals(25, loadRing.getXTilt());
        assertEquals(50, loadRing.getZTilt());
    }
}
