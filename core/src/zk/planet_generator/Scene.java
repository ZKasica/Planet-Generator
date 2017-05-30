package zk.planet_generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import zk.planet_generator.generators.NoiseGenerator;
import zk.planet_generator.generators.ObjectGenerator;
import zk.planet_generator.scene_objects.*;

/**
 * Created by zach on 5/21/17.
 */
public class Scene extends InputAdapter {
    public static final int BUFFER_WIDTH = 640;
    public static final int BUFFER_HEIGHT = 360;
    public static final int CENTER_X = BUFFER_WIDTH / 2;
    public static final int CENTER_Y = BUFFER_HEIGHT / 2;
    public static final int EDITOR_OFFSET = 100;

    public static ShaderProgram planetShader;

    private OrthographicCamera gameCamera;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PixelBuffer pixelBuffer;

    private ObjectGenerator objectGenerator;
    private Array<SpaceObject> spaceObjects;
    private Planet planet;

    private boolean shouldSpeedUpTime;

    public Scene() {
        setupRendering();
        //generateObjects();
        createEmptyScene();
    }

    private void setupRendering() {
        ShaderProgram.pedantic = false;
        planetShader = new ShaderProgram(Gdx.files.internal("shaders/planet.vsh"), Gdx.files.internal("shaders/planet.fsh"));
        if(!planetShader.isCompiled()) {
            Gdx.app.error("Planet Shader", "\n" + planetShader.getLog());
        }

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, BUFFER_WIDTH, BUFFER_HEIGHT);
        gameCamera.zoom = 1f;
        gameCamera.translate(EDITOR_OFFSET, 0);
        gameCamera.update();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        batch = new SpriteBatch();
        pixelBuffer = new PixelBuffer();
    }

    public void createEmptyScene() {
        spaceObjects = new Array<>();

        createPlanet(MathUtils.randomSign());
        objectGenerator = new ObjectGenerator(this);
    }

    public void generateObjects() {
        spaceObjects = new Array<>();

        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();

        createPlanet(velDir);

        objectGenerator = new ObjectGenerator(this, velDir, zDir);
        objectGenerator.createMoons();
        objectGenerator.createRings();
        objectGenerator.createClouds();
        objectGenerator.createStars();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;

            case Input.Keys.R:
                reset();
                return true;

            case Input.Keys.E:
                shouldSpeedUpTime = true;
                return true;

            case Input.Keys.F12:
                takeScreenshot();
                return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.E:
                shouldSpeedUpTime = false;
                return true;
        }

        return false;
    }

    private Sprite generatePlanetSprite(int size) {
        return new Sprite(generatePlanetTexture(size));
    }

    private void createPlanet(int velDir) {
        Sprite planet = generatePlanetSprite(1024);
        int size = MathUtils.random(100, 148);
        planet.setSize(size, size);
        planet.setPosition(CENTER_X - planet.getWidth() / 2, CENTER_Y - planet.getHeight() / 2);
        this.planet = new Planet(planet, velDir);
        spaceObjects.add(this.planet);
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if(shouldSpeedUpTime) {
            delta *= 10;
        }

        for(SpaceObject spaceObject : spaceObjects) {
            spaceObject.update(delta);
        }
        spaceObjects.sort();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(25f / 255f, 20f / 255f, 30f / 255f, 1);

        pixelBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(30f / 255f, 25f / 255f, 35f / 255f, 1);

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        for(SpaceObject spaceObject : spaceObjects) {
            spaceObject.render(batch);
        }
        batch.end();
        pixelBuffer.end();

        batch.setShader(null);
        pixelBuffer.render(batch, camera);
    }

    public void dispose() {
        batch.dispose();
        pixelBuffer.dispose();
        planetShader.dispose();
    }

    private Texture generatePlanetTexture(int size) {
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

        Texture planetTexture = new Texture(pixmap);
        pixmap.dispose();
        return planetTexture;
    }

    public void reset() {
        for(SpaceObject object : spaceObjects) {
            object.getSprite().getTexture().dispose();
        }
        spaceObjects.clear();

        generateObjects();
    }

    public void addSpaceObjects(Array<? extends SpaceObject> objects) {
        spaceObjects.addAll(objects);
    }

    public void addSpaceObject(SpaceObject object) {
        spaceObjects.add(object);
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

    public void removeObject(SpaceObject object) {
        spaceObjects.removeValue(object, false);
    }
}