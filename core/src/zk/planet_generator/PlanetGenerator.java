package zk.planet_generator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Planet shader code: https://gamedev.stackexchange.com/questions/9346/2d-shader-to-draw-representation-of-rotating-sphere
 * Noise generator code: http://devmag.org.za/2009/04/25/perlin-noise/
 */
public class PlanetGenerator extends ApplicationAdapter {
    public static final int BUFFER_WIDTH = 640;
    public static final int BUFFER_HEIGHT = 360;

    private OrthographicCamera gameCamera;
    private OrthographicCamera camera;

    private ShaderProgram planetShader;
    private SpriteBatch batch;
    private PixelBuffer pixelBuffer;
    private Sprite planet;

    private float time;
    private float speed = 1/10f;

    @Override
    public void create() {
        ShaderProgram.pedantic = false;
        planetShader = new ShaderProgram(Gdx.files.internal("shaders/planet.vsh"), Gdx.files.internal("shaders/planet.fsh"));
        if(!planetShader.isCompiled()) {
            Gdx.app.error("Shader", planetShader.getLog());
        }

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, BUFFER_WIDTH, BUFFER_HEIGHT);
        //gameCamera.rotate(MathUtils.random(0, 360));
        gameCamera.update();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        batch = new SpriteBatch();
        pixelBuffer = new PixelBuffer();

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown (int keycode) {
                switch(keycode) {
                    case Input.Keys.ESCAPE:
                        Gdx.app.exit();
                        return true;

                    case Input.Keys.R:
                        generateRandomPlanet();
                        return true;
                }

                return false;
            }
        });

        generateRandomPlanet();
    }

    private void generateRandomPlanet() {
        planet = new Sprite(generatePlanetTexture(256));
        planet.setSize(128, 128);
        planet.setPosition(gameCamera.viewportWidth / 2 - planet.getWidth() / 2, gameCamera.viewportHeight / 2 - planet.getHeight() / 2);
    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime() * speed;

        planetShader.begin();
        planetShader.setUniformf("time", time);
        planetShader.end();

        pixelBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(37f / 255f, 38f / 255f, 54f / 255f, 1);

        batch.setShader(planetShader);
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        planet.draw(batch);
        batch.end();
        pixelBuffer.end();

        batch.setShader(null);
        pixelBuffer.render(batch, camera);
    }

    @Override
    public void dispose() {

    }

    private Texture generatePlanetTexture(int size) {
        float[][] generated = NoiseGenerator.GenerateWhiteNoise(size, size);
        generated = NoiseGenerator.GeneratePerlinNoise(generated, 7);

        Pixmap pixmap = new Pixmap(generated.length - 1, generated.length - 1, Pixmap.Format.RGBA8888);
        for (int x = 0; x < generated.length - 1; x++) {
            for (int y = 0; y < generated.length - 1; y++) {
                double value = generated[x][y];

                if (value < 0.55f) {
                    pixmap.drawPixel(x, y, Color.rgba8888(62f / 255f, 120f / 255f, 160f / 255f, 1f));
                } else if (value < 0.57f) {
                    pixmap.drawPixel(x, y, Color.rgba8888(220f / 255f, 235f / 255f, 175f / 255f, 1f));
                } else {
                    pixmap.drawPixel(x, y, Color.rgba8888(146f / 255f, 209f / 255f, 135f / 255f, 1f));
                }
            }
        }

        Texture planetTexture = new Texture(pixmap);
        pixmap.dispose();
        return planetTexture;
    }
}
