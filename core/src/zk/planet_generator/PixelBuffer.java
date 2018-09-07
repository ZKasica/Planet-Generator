package zk.planet_generator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class PixelBuffer extends FrameBuffer {
    private TextureRegion pixelBufferRegion;

    public PixelBuffer(int width, int height) {
        super(Pixmap.Format.RGBA8888, width, height, false);
        getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        pixelBufferRegion = new TextureRegion(getColorBufferTexture(), 0, 0, width, height);
        pixelBufferRegion.flip(false, true);
    }

    public void render(SpriteBatch batch, OrthographicCamera screenCamera) {
        batch.setShader(null);
        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        batch.draw(pixelBufferRegion, 0, 0, screenCamera.viewportWidth, screenCamera.viewportHeight);
        batch.end();
    }
}
