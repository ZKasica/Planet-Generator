package zk.planet_generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import zk.planet_generator.generators.NoiseGenerator;
import zk.planet_generator.generators.ObjectGenerator;
import zk.planet_generator.scene_objects.*;

public class Scene extends InputAdapter implements Disposable, Json.Serializable {
    public static final int BUFFER_WIDTH = 640;
    public static final int BUFFER_HEIGHT = 360;
    public static final int CENTER_X = BUFFER_WIDTH / 2;
    public static final int CENTER_Y = BUFFER_HEIGHT / 2;
    public static final int EDITOR_OFFSET = 100;
    public static final int STAR_EDITOR_OFFSET = 15;
    public static final float TRANSITION_DURATION = 0.7f;
    public static final Texture pixelTexture = new Texture(Gdx.files.internal("pixel.png"));

    public static Color backgroundColor = new Color(30f / 255f, 25f / 255f, 35f / 255f, 1);

    private OrthographicCamera gameCamera;
    private OrthographicCamera starCamera;
    private OrthographicCamera screenCamera;
    private SpriteBatch batch;
    private PixelBuffer pixelBuffer;

    private ObjectGenerator objectGenerator;
    private Planet planet;
    private Array<SpaceObject> spaceObjects;
    private Array<Ring> rings;
    private Array<Cloud> clouds;
    private Array<Star> stars;
    private Array<Orbiter> moons;
    private Array<Trajectory> trajectories;

    private boolean shouldSpeedUpTime;

    private boolean focus;
    private float elapsed;
    private float lifetime;
    private float startX;
    private float targetX;
    private float startStarX;
    private float targetStarX;

    public Scene() {
        setupRendering();

        spaceObjects = new Array<>();
        rings = new Array<>();
        clouds = new Array<>();
        stars = new Array<>();
        moons = new Array<>();
        trajectories = new Array<>();

        createEmptyScene();
    }

    private void setupRendering() {
        ShaderProgram.pedantic = false;

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, BUFFER_WIDTH, BUFFER_HEIGHT);
        gameCamera.translate(EDITOR_OFFSET, 0);
        gameCamera.update();

        starCamera = new OrthographicCamera();
        starCamera.setToOrtho(false, BUFFER_WIDTH, BUFFER_HEIGHT);
        starCamera.translate(STAR_EDITOR_OFFSET, 0);
        starCamera.update();

        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screenCamera.update();

        batch = new SpriteBatch();
        pixelBuffer = new PixelBuffer(BUFFER_WIDTH, BUFFER_HEIGHT);
    }

    public void createEmptyScene() {
        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();
        createPlanet(MathUtils.randomSign());
        objectGenerator = new ObjectGenerator(this, velDir, zDir);
    }

    public void generateObjects() {
        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();

        createPlanet(velDir);

        objectGenerator = new ObjectGenerator(this, velDir, zDir);
        objectGenerator.createMoons();

        rings = new Array<>();
        while(rings.size == 0) {
            objectGenerator.createRings();
        }
        objectGenerator.createClouds(Color.rgba8888(245f / 255f, 245f / 255f, 213f / 255f, 1f));
        objectGenerator.createStars();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;

            case Input.Keys.F12:
                takeScreenshot();
                return true;
        }

        return false;
    }

    public void update(float delta) {
        if(shouldSpeedUpTime) {
            delta *= 10;
        }

        tryToTransition(delta);
        updateObjects(delta);
        drawObjects();
    }

    private void tryToTransition(float delta) {
        if(focus) {
            elapsed += delta;
            float progress = Math.min(1f, elapsed / lifetime);

            gameCamera.position.x = Interpolation.circleOut.apply(startX, targetX, progress);
            gameCamera.update();

            starCamera.position.x = Interpolation.circleOut.apply(startStarX, targetStarX, progress);
            starCamera.update();

            if(progress == 1) {
                focus = false;
            }
        }
    }

    private void updateObjects(float delta) {
        for(Trajectory trajectory : trajectories) {
            trajectory.update();
        }

        for(SpaceObject spaceObject : spaceObjects) {
            spaceObject.update(delta);
        }
        spaceObjects.sort();
    }

    private void drawObjects() {
        pixelBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);

        batch.begin();
        batch.setProjectionMatrix(starCamera.combined);
        for(Star star : stars) {
            star.render(batch);
        }

        batch.setProjectionMatrix(gameCamera.combined);
        for(SpaceObject spaceObject : spaceObjects) {
            spaceObject.render(batch);
        }
        batch.end();
        pixelBuffer.end();

        pixelBuffer.render(batch, screenCamera);
    }

    @Override
    public void dispose() {
        batch.dispose();
        pixelBuffer.dispose();
        planet.dispose();
    }

    private void createPlanet(int velDir) {
        Pixmap pixmap = generatePlanetPixmap(1024);
        Sprite planet = new Sprite(new Texture(pixmap));
        int size = MathUtils.random(100, 148);
        planet.setSize(size, size);
        planet.setPosition(CENTER_X - planet.getWidth() / 2, CENTER_Y - planet.getHeight() / 2);
        this.planet = new Planet(planet, pixmap, velDir);
        spaceObjects.add(this.planet);
    }

    private Pixmap generatePlanetPixmap(int size) {
        float[][] generated = NoiseGenerator.GenerateWhiteNoise(size, size);
        generated = NoiseGenerator.GeneratePerlinNoise(generated, 8);

        Pixmap pixmap = new Pixmap(generated.length, generated.length, Pixmap.Format.RGBA8888);
        for (int x = 0; x < generated.length; x++) {
            for (int y = 0; y < generated.length; y++) {
                double value = generated[x][y];

                if(value < 0.40f) {
                    // Deep ocean
                    pixmap.drawPixel(x, y, Color.rgba8888(47f / 255f, 86f / 255f, 118f / 255f, 1f));
                } else if (value < 0.55f) {
                    // Ocean
                    pixmap.drawPixel(x, y, Color.rgba8888(62f / 255f, 120f / 255f, 160f / 255f, 1f));
                } else {
                    // Land
                    pixmap.drawPixel(x, y, Color.rgba8888(146f / 255f, 209f / 255f, 135f / 255f, 1f));
                }
            }
        }
        return pixmap;
    }

    public void clear() {
        for(SpaceObject object : spaceObjects) {
            object.getSprite().getTexture().dispose();
        }
        spaceObjects.clear();
        rings.clear();
        clouds.clear();
        moons.clear();
        stars.clear();
        planet.dispose();
    }

    private void takeScreenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(Gdx.files.external("screenshot.png"), pixmap);
        Gdx.app.log("Screenshot", "Saved screenshot to screenshot.png");
        pixmap.dispose();
    }

    public Planet getPlanet() {
        return planet;
    }

    public ObjectGenerator getObjectGenerator() {
        return objectGenerator;
    }

    private void removeObjects(Array<? extends SpaceObject> objects) {
        spaceObjects.removeAll(objects, false);
    }

    private void removeObject(SpaceObject object) {
        spaceObjects.removeValue(object, false);
    }

    public Array<Ring> getRings() {
        return rings;
    }

    public void addRingObject(Orbiter ringObject) {
        spaceObjects.add(ringObject);
    }

    public void addRing(Ring ring) {
        rings.add(ring);
        spaceObjects.addAll(ring.getObjects());
    }

    public void removeRingObject(Orbiter orbiter) {
        spaceObjects.removeValue(orbiter, false);
    }

    public void removeRing(Ring ring) {
        rings.removeValue(ring, false);
        removeObjects(ring.getObjects());
    }

    public Array<Star> getStars() {
        return stars;
    }

    public void addStar(Star star) {
        stars.add(star);
    }

    public void removeStar(Star star) {
        stars.removeValue(star, false);
    }

    public Array<Cloud> getClouds() {
        return clouds;
    }

    public void addCloud(Cloud cloud) {
        clouds.add(cloud);
        spaceObjects.addAll(cloud.getCloudObjects());
    }

    public void removeCloud(Cloud cloud) {
        clouds.removeValue(cloud, false);
        removeObjects(cloud.getCloudObjects());
    }

    public Array<Orbiter> getMoons() {
        return moons;
    }

    public void addMoon(Orbiter orbiter) {
        moons.add(orbiter);
        spaceObjects.add(orbiter);
    }

    public void removeMoon(Orbiter orbiter) {
        moons.removeValue(orbiter, false);
        removeObject(orbiter);
    }

    public Trajectory addTrajectory(Orbiter orbiter) {
        Trajectory trajectory = new Trajectory(orbiter);
        trajectories.add(trajectory);
        addRing(trajectory.getPath());
        return trajectory;
    }

    public void removeTrajectory(Trajectory trajectory) {
        trajectories.removeValue(trajectory, false);
        removeRing(trajectory.getPath());
    }

    public void focusOnPlanet() {
        focus = true;
        lifetime = TRANSITION_DURATION;
        elapsed = 0;

        startX = CENTER_X + EDITOR_OFFSET;
        targetX = CENTER_X;
        startStarX = CENTER_X + STAR_EDITOR_OFFSET;
        targetStarX = CENTER_X;
    }

    public void focusOnUI() {
        focus = true;
        lifetime = TRANSITION_DURATION;
        elapsed = 0;

        startX = CENTER_X;
        targetX = CENTER_X + EDITOR_OFFSET;
        startStarX = CENTER_X;
        targetStarX = CENTER_X + STAR_EDITOR_OFFSET;
    }

    @Override
    public void write(Json json) {
        json.writeValue("object_generator", objectGenerator);
        json.writeValue("planet", planet);
        json.writeValue("rings", rings);
        json.writeValue("stars", stars);
        json.writeValue("moons", moons);
        json.writeValue("clouds", clouds);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        objectGenerator = json.readValue("object_generator", ObjectGenerator.class, jsonData);
        objectGenerator.setScene(this);

        loadPlanet(json, jsonData);
        loadRings(json, jsonData);
        loadStars(json, jsonData);
        loadMoons(json, jsonData);
        loadClouds(json, jsonData);
    }


    private void loadRings(Json json, JsonValue jsonData) {
        rings = json.readValue("rings", Array.class, new Array(), jsonData);
        for(Ring ring : rings) {
            for(int i = 0; i < ring.getBaseObjectCount(); i++) {
                objectGenerator.createObjectInRing(ring);
            }
        }
    }

    private void loadStars(Json json, JsonValue jsonData) {
        stars = json.readValue("stars", Array.class, new Array(), jsonData);
        for(Star star : stars) {
            star.getSprite().setTexture(pixelTexture);
        }
    }

    private void loadMoons(Json json, JsonValue jsonData) {
        Array<Orbiter> moons = json.readValue("moons", Array.class, new Array(), jsonData);
        for(Orbiter moon : moons) {
            moon.setSprite(objectGenerator.createMoonSprite(moon.getSize()));
            moon.setColor(moon.getColor());
            addMoon(moon);
        }
    }

    private void loadClouds(Json json, JsonValue jsonData) {
        Array<Cloud> clouds = json.readValue("clouds", Array.class, new Array(), jsonData);
        int cloudColor = Color.rgba8888(245f / 255f, 245f / 255f, 213f / 255f, 1f);
        for(Cloud cloud : clouds) {
            for(int i = 0; i < cloud.getCloudObjects().size; i++) {
                Orbiter cloudObject = cloud.getCloudObjects().get(i);
                cloudObject.setSprite(objectGenerator.createMoonSprite(cloudObject.getSize()));
                cloudObject.setColor(cloudColor);
            }
            addCloud(cloud);
        }
    }

    private void loadPlanet(Json json, JsonValue jsonData) {
        planet = json.readValue("planet", Planet.class, jsonData);

        Pixmap loadPixmap = new Pixmap(1024, 1024, Pixmap.Format.RGBA8888);
        int index = 0;
        for(int x = 0; x < 1024; x++) {
            for (int y = 0; y < 1024; y++) {
                int color = 0;
                switch (planet.getTextureString().charAt(index)) {
                    case 'd':
                        color = Color.rgba8888(47f / 255f, 86f / 255f, 118f / 255f, 1f);
                        break;

                    case 'o':
                        color = Color.rgba8888(62f / 255f, 120f / 255f, 160f / 255f, 1f);
                        break;

                    case 'l':
                        color = Color.rgba8888(146f / 255f, 209f / 255f, 135f / 255f, 1f);
                        break;
                }

                index++;

                loadPixmap.setColor(color);
                loadPixmap.drawPixel(x, y);
            }
        }

        Sprite sprite = new Sprite(new Texture(loadPixmap));
        sprite.setSize(planet.getSize(), planet.getSize());
        sprite.setPosition(CENTER_X - planet.getSize() / 2, CENTER_Y - planet.getSize() / 2);
        planet = new Planet(sprite, loadPixmap, objectGenerator.getVelDir());

        spaceObjects.add(planet);
    }
}