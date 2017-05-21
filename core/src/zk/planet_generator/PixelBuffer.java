package zk.planet_generator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Created by Zach on 10/1/2016.
 */
public class PixelBuffer extends FrameBuffer {
    private TextureRegion pixelBufferRegion;

    public PixelBuffer() {
        super(Pixmap.Format.RGBA8888, PlanetGeneratorGame.BUFFER_WIDTH, PlanetGeneratorGame.BUFFER_HEIGHT, false);
        getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        pixelBufferRegion = new TextureRegion(getColorBufferTexture(), 0, 0, PlanetGeneratorGame.BUFFER_WIDTH, PlanetGeneratorGame.BUFFER_HEIGHT);
        pixelBufferRegion.flip(false, true);
    }

    public void render(SpriteBatch batch, OrthographicCamera screenCamera) {
        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        batch.draw(pixelBufferRegion, 0, 0, screenCamera.viewportWidth, screenCamera.viewportHeight);
        batch.end();
    }
}
