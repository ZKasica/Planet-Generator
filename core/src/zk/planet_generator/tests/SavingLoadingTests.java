package zk.planet_generator.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.planet_generator.ColorGroup;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;
import zk.planet_generator.scene_objects.Star;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by zach on 6/5/17.
 */
public class SavingLoadingTests {
    private Json json;

    @BeforeEach
    public void beforeTests() {
        json = new Json();
    }

    @Test
    public void testRingJson() {
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
        assertNotEquals("", ringJson);

        Ring loadRing = json.fromJson(Ring.class, ringJson);

        assertNotEquals(null, loadRing);
        assertEquals(10, loadRing.getMinimumRadius());
        assertEquals(50, loadRing.getMaximumRadius());
        assertEquals(100, loadRing.getAngularVelocity());
        assertEquals(25, loadRing.getXTilt());
        assertEquals(50, loadRing.getZTilt());
    }

    @Test
    public void testStar1Json() {
        // Test star with size 1
        Sprite sprite = new Sprite();
        sprite.setSize(1, 1);
        Star saveStar = new Star(sprite);
        saveStar.getSprite().setPosition(100, 150);

        String starJson = json.toJson(saveStar);
        assertNotEquals("", starJson);

        Star loadStar = json.fromJson(Star.class, starJson);

        assertNotEquals(null, loadStar);
        assertNotEquals(null, loadStar.getSprite());
        assertEquals(1, loadStar.getSprite().getWidth());
        assertEquals(1, loadStar.getSprite().getHeight());
        assertEquals(100, loadStar.getSprite().getX());
        assertEquals(150, loadStar.getSprite().getY());
    }

    @Test
    public void testStar2Json() {
        Sprite sprite = new Sprite();
        Star saveStar = new Star(sprite);
        saveStar.getSprite().setPosition(150, 100);
        sprite.setSize(2, 2);

        String starJson = json.toJson(saveStar);
        assertNotEquals("", starJson);

        Star loadStar = json.fromJson(Star.class, starJson);

        assertNotEquals(null, loadStar);
        assertNotEquals(null, loadStar.getSprite());
        assertEquals(2, loadStar.getSprite().getWidth());
        assertEquals(2, loadStar.getSprite().getHeight());
        assertEquals(150, loadStar.getSprite().getX());
        assertEquals(100, loadStar.getSprite().getY());
    }
}
