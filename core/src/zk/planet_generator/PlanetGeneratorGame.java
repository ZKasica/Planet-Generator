package zk.planet_generator;

import com.badlogic.gdx.ApplicationAdapter;
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
 * Planet shader code: https://gamedev.stackexchange.com/questions/9346/2d-shader-to-draw-representation-of-rotating-sphere
 * Noise generator code: http://devmag.org.za/2009/04/25/perlin-noise/
 */
public class PlanetGeneratorGame extends ApplicationAdapter {
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
    private Planet planet;

    private boolean shouldSpeedUpTime;

    @Override
    public void create() {
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
        spaceObjects = new Array<SpaceObject>();

        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();

        createPlanet(velDir);
        createMoons(velDir, zDir);
        createRings(velDir, zDir);
        createClouds(velDir);
        createStars();
    }

    private void createMoons(int velDir, int zDir) {
        // TODO: Moons outside of outer rings radius

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
            spaceObjects.add(new Orbiter(createMoon(colors.random(), MathUtils.random(16, 20) / (moonCount / 3 + 1)), moonBlueprint));
        }
    }

    private void createRings(int velDir, int zDir) {
        boolean shouldGenerateRings = MathUtils.randomBoolean();
        if(!shouldGenerateRings) {
            return;
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
        float minimumRadius = planet.getWidthAtY(0) + 25;
        float maximumRadius = minimumRadius + MathUtils.random(20, 60);
        ColorGroup colorGroup = colors.random();
        colors.removeValue(colorGroup, true);

        for(int i = 0; i < objectCount; i++) {
            Orbiter.OrbiterBlueprint blueprint = new Orbiter.OrbiterBlueprint();
            blueprint.angularVelocity = angularVelocity;
            blueprint.zTilt = zTilt;
            blueprint.xTilt = xTilt;
            blueprint.angle = MathUtils.random(0, 360);
            blueprint.radius = MathUtils.random(minimumRadius, maximumRadius);

            if (MathUtils.randomBoolean(0.9f)) {
                spaceObjects.add(new Orbiter(createMoon(colorGroup.random(), MathUtils.random(4, 6)), blueprint));
            } else {
                spaceObjects.add(new Orbiter(createSquare(colorGroup.random(), MathUtils.random(4, 5)), blueprint));
            }
        }

        boolean shouldGenerateOuterRings = MathUtils.randomBoolean();
        if(!shouldGenerateOuterRings) {
            return;
        }

        objectCount = MathUtils.random(200, 350);
        angularVelocity = angularVelocity - velDir * MathUtils.random(5, 15);
        minimumRadius = maximumRadius + 5 + MathUtils.random(5);
        maximumRadius = minimumRadius + MathUtils.random(20, 50);
        colorGroup = colors.random();
        colors.removeValue(colorGroup, true);

        for(int i = 0; i < objectCount; i++) {
            Orbiter.OrbiterBlueprint blueprint = new Orbiter.OrbiterBlueprint();
            blueprint.angularVelocity = angularVelocity;
            blueprint.zTilt = zTilt;
            blueprint.xTilt = xTilt;
            blueprint.angle = MathUtils.random(0, 360);
            blueprint.radius = MathUtils.random(minimumRadius, maximumRadius);

            if (MathUtils.randomBoolean(0.9f)) {
                spaceObjects.add(new Orbiter(createMoon(colorGroup.random(), MathUtils.random(4, 6)), blueprint));
            } else {
                spaceObjects.add(new Orbiter(createSquare(colorGroup.random(), MathUtils.random(4, 5)), blueprint));
            }
        }
    }

    private void createClouds(int velDir) {
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
                cloudBlueprint.radius = planet.getMinimumCloudRadiusAtY(cloudBlueprint.yOffset) + MathUtils.random(0, 6);

                spaceObjects.add(new Orbiter(createMoon(cloudColor, MathUtils.random(5, 8)), cloudBlueprint));
            }
        }
    }

    private void createStars() {
        stars = new Array<Star>();
        float starAmount  = MathUtils.random(20, 100);
        Texture pixelTexture = new Texture(Gdx.files.internal("pixel.png"));
        for(int i = 0; i < starAmount; i++) {
            Sprite star = new Sprite(pixelTexture);
            star.setPosition(MathUtils.random(0, BUFFER_WIDTH), MathUtils.random(0, BUFFER_HEIGHT));
            star.setColor(Color.WHITE);

            if(MathUtils.randomBoolean(0.1f)) {
                star.setSize(2, 2);
            }

            stars.add(new Star(star));
        }
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

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

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

    @Override
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

    private Sprite createMoon(int color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, size / 2 - 1);
        Sprite sprite = new Sprite(new Texture(pixmap));
        pixmap.dispose();
        return sprite;
    }

    private Sprite createSquare(int color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, size, size);
        Sprite sprite = new Sprite(new Texture(pixmap));
        pixmap.dispose();
        return sprite;
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

        generateObjects();
    }
}