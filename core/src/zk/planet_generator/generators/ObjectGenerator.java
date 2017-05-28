package zk.planet_generator.generators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.xpath.internal.operations.Or;
import zk.planet_generator.ColorGroup;
import zk.planet_generator.Scene;
import zk.planet_generator.scene_objects.*;

/**
 * Created by zach on 5/26/17.
 */
public class ObjectGenerator {
    private Scene scene;
    private int velDir;
    private int zDir;

    public ObjectGenerator(Scene scene) {
        this(scene, 0, 0);
    }

    public ObjectGenerator(Scene scene, int velDir, int zDir) {
        this.scene = scene;
        this.velDir = velDir;
        this.zDir = zDir;
    }

    public Array<Orbiter> createMoons() {
        // TODO: Moons outside of outer rings radius
        Array<Orbiter> moons = new Array<>();

        ColorGroup colors = new ColorGroup()
                .add(Color.rgba8888(176f / 255f, 155f / 255f, 178f / 255f, 1f))
                .add(Color.rgba8888(156f / 255f, 155f / 255f, 190f / 255f, 1f))
                .add(Color.rgba8888(223f / 255f, 233f / 255f, 180f / 255f, 1f))
                .add(Color.rgba8888(75f / 255f, 109f / 255f, 133f / 255f, 1f));

        int moonCount = MathUtils.random(0, 8);

        float zTilt = zDir * MathUtils.random(10, 50);
        float xTilt = -MathUtils.random(15, 55);

        for(int i = 0; i < moonCount; i++) {
            Orbiter.OrbiterBlueprint moonBlueprint = new Orbiter.OrbiterBlueprint();
            moonBlueprint.angularVelocity = velDir * MathUtils.random(15, 60);
            moonBlueprint.zTilt = zTilt + MathUtils.random(-10, 10);
            moonBlueprint.xTilt = xTilt + MathUtils.random(-10, 10);
            moonBlueprint.angle = MathUtils.random(0, 360);
            moonBlueprint.radius = MathUtils.random(100, 250);

            Orbiter moon = new Orbiter(createMoon(colors.random(), MathUtils.random(16, 20) / (moonCount / 3 + 1)), moonBlueprint);
            moons.add(moon);
        }

        scene.addSpaceObjects(moons);

        return moons;
    }

    public Array<Ring> createRings() {
        Array<Ring> rings = new Array<>();

        boolean shouldGenerateRings = MathUtils.randomBoolean();
        if(!shouldGenerateRings) {
            return rings;
        }

        Array<ColorGroup> colors = new Array<ColorGroup>();

        colors.add(new ColorGroup()
                .add(Color.rgba8888(223f / 255f, 233f / 255f, 180f / 255f, 1f))
                .add(Color.rgba8888(170f / 255f, 90f / 255f, 103f / 255f, 1f)));

        colors.add(new ColorGroup()
                .add(Color.rgba8888(191f / 255f, 231f / 255f, 231f / 255f, 1f))
                .add(Color.rgba8888(75f / 255f, 109f / 255f, 133f / 255f, 1f)));

        colors.add(new ColorGroup()
                .add(Color.rgba8888(169f / 255f, 194f / 255f, 175f / 255f, 1f))
                .add(Color.rgba8888(30f / 255f, 97f / 255f, 42f / 255f, 1f)));

        int objectCount = MathUtils.random(200, 300);
        float angularVelocity = velDir * MathUtils.random(20, 35);
        float zTilt = zDir * MathUtils.random(10, 50);
        float xTilt = -MathUtils.random(10, 40);
        float minimumRadius = scene.getPlanet().getWidthAtY(0) + 25;
        float maximumRadius = minimumRadius + MathUtils.random(20, 60);
        ColorGroup colorGroup = colors.random();
        colors.removeValue(colorGroup, true);

        Array<Orbiter> ringObjects = new Array<>();
        for(int i = 0; i < objectCount; i++) {
            Orbiter.OrbiterBlueprint blueprint = new Orbiter.OrbiterBlueprint();
            blueprint.angularVelocity = angularVelocity;
            blueprint.zTilt = zTilt;
            blueprint.xTilt = xTilt;
            blueprint.angle = MathUtils.random(0, 360);
            blueprint.radius = MathUtils.random(minimumRadius, maximumRadius);

            Orbiter ringObject = null;
            if (MathUtils.randomBoolean(0.9f)) {
                ringObject = new Orbiter(createMoon(colorGroup.random(), MathUtils.random(4, 6)), blueprint);
            } else {
                ringObject = new Orbiter(createSquare(colorGroup.random(), MathUtils.random(4, 5)), blueprint);
            }
            ringObjects.add(ringObject);
        }

        scene.addSpaceObjects(ringObjects);
        rings.add(new Ring(ringObjects));

        boolean shouldGenerateOuterRings = MathUtils.randomBoolean();
        if(!shouldGenerateOuterRings) {
            return rings;
        }

        objectCount = MathUtils.random(200, 350);
        angularVelocity = angularVelocity - velDir * MathUtils.random(5, 15);
        minimumRadius = maximumRadius + 5 + MathUtils.random(5);
        maximumRadius = minimumRadius + MathUtils.random(20, 50);
        colorGroup = colors.random();
        colors.removeValue(colorGroup, true);

        Array<Orbiter> ringObjects2 = new Array<>();
        for(int i = 0; i < objectCount; i++) {
            Orbiter.OrbiterBlueprint blueprint = new Orbiter.OrbiterBlueprint();
            blueprint.angularVelocity = angularVelocity;
            blueprint.zTilt = zTilt;
            blueprint.xTilt = xTilt;
            blueprint.angle = MathUtils.random(0, 360);
            blueprint.radius = MathUtils.random(minimumRadius, maximumRadius);

            Orbiter ringObject = null;
            if (MathUtils.randomBoolean(0.9f)) {
                ringObject = new Orbiter(createMoon(colorGroup.random(), MathUtils.random(4, 6)), blueprint);
            } else {
                ringObject = new Orbiter(createSquare(colorGroup.random(), MathUtils.random(4, 5)), blueprint);
            }
            ringObjects2.add(ringObject);
        }
        scene.addSpaceObjects(ringObjects2);
        rings.add(new Ring(ringObjects2));

        return rings;
    }

    public Array<Orbiter> createClouds() {
        Array<Orbiter> clouds = new Array<>();

        int cloudColor = Color.rgba8888(245f / 255f, 245f / 255f, 213f / 255f, 1f);

        int cloudCount = MathUtils.random(10, 40);
        int velocity = MathUtils.random(15, 30);
        for(int i = 0; i < cloudCount; i++) {
            int clusterCount = MathUtils.random(6, 40);
            int yOffset = MathUtils.random(-56, 56);
            int angle = MathUtils.random(0, 360);

            for(int j = 0; j < clusterCount; j++) {
                Orbiter.OrbiterBlueprint cloudBlueprint = new Orbiter.OrbiterBlueprint();
                cloudBlueprint.angularVelocity = velDir * velocity;
                cloudBlueprint.zTilt = 0;
                cloudBlueprint.xTilt = -15;
                cloudBlueprint.angle = angle + MathUtils.random(0, 30);
                cloudBlueprint.yOffset = yOffset + MathUtils.random(-5, 5);
                cloudBlueprint.radius = scene.getPlanet().getMinimumCloudRadiusAtY(cloudBlueprint.yOffset) + MathUtils.random(0, 6);

                Orbiter cloud = new Orbiter(createMoon(cloudColor, MathUtils.random(5, 8)), cloudBlueprint);
                clouds.add(cloud);
            }
        }

        scene.addSpaceObjects(clouds);
        return clouds;
    }

    public Array<Star> createStars() {
        return createStars(MathUtils.random(20, 120));
    }

    public Array<Star> createStars(int starAmount) {
        Array<Star> stars = new Array<>();
        Texture pixelTexture = new Texture(Gdx.files.internal("pixel.png"));
        for(int i = 0; i < starAmount; i++) {
            Sprite star = new Sprite(pixelTexture);
            star.setPosition(MathUtils.random(0, Scene.BUFFER_WIDTH * 2), MathUtils.random(0, Scene.BUFFER_HEIGHT));
            star.setColor(Color.WHITE);

            if(MathUtils.randomBoolean(0.1f)) {
                star.setSize(2, 2);
            }

            stars.add(new Star(star));
        }

        scene.addSpaceObjects(stars);
        return stars;
    }

    protected Sprite createMoon(int color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, size / 2 - 1);
        Sprite sprite = new Sprite(new Texture(pixmap));
        pixmap.dispose();
        return sprite;
    }

    protected Sprite createSquare(int color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, size, size);
        Sprite sprite = new Sprite(new Texture(pixmap));
        pixmap.dispose();
        return sprite;
    }
}