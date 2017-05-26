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

/**
 * Created by zach on 5/21/17.
 */
public class Scene {
    public static final int BUFFER_WIDTH = 640;
    public static final int BUFFER_HEIGHT = 360;
    public static final int CENTER_X = BUFFER_WIDTH / 2;
    public static final int CENTER_Y = BUFFER_HEIGHT / 2;

    public static ShaderProgram planetShader;

    private OrthographicCamera gameCamera;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PixelBuffer pixelBuffer;

    private Array<Star> stars;
    private Array<SpaceObject> spaceObjects;
    private Array<Orbiter> moons;
    private Array<Orbiter> rings;
    private Array<Orbiter> clouds;
    private Planet planet;

    private boolean shouldSpeedUpTime;

    public Scene() {
        setupRendering();
        generateObjects();
        initializeInput();
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
        gameCamera.update();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        batch = new SpriteBatch();
        pixelBuffer = new PixelBuffer();
    }

    private void generateObjects() {
        spaceObjects = new Array<>();

        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();

        createPlanet(velDir);

        ObjectGenerator objectGenerator = new ObjectGenerator(planet);
        moons = objectGenerator.createMoons(velDir, zDir);
        rings = objectGenerator.createRings(velDir, zDir);
        clouds = objectGenerator.createClouds(velDir);
        stars = objectGenerator.createStars();

        spaceObjects.addAll(moons);
        spaceObjects.addAll(rings);
        spaceObjects.addAll(clouds);
    }

    private void initializeInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown (int keycode) {
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
        });
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

//        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
//            for(Orbiter ring : rings) {
//                ring.updateZTilt(50 * delta);
//            }
//        }

        if(shouldSpeedUpTime) {
            delta *= 10;
        }

        for(SpaceObject spaceObject : spaceObjects) {
            spaceObject.update(delta);
        }
        spaceObjects.sort();

        pixelBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(25f / 255f, 20f / 255f, 30f / 255f, 1);
        //Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        for(Star star : stars) {
            //star.update(delta);
            star.render(batch);
        }

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

        for(Star star : stars) {
            star.getSprite().getTexture().dispose();
        }
        stars.clear();

        moons.clear();
        rings.clear();
        clouds.clear();

        generateObjects();
    }
}