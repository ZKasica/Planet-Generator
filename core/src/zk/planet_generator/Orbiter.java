package zk.planet_generator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Zach on 5/16/2017.
 */
public class Orbiter extends SpaceObject {
    private float angularVelocity;
    private float zTilt; // Affects how steep of an angle it orbits at
    private float xTilt; // Affects how vertical it appears to the viewer
    private float angle;
    private float radius;

    public Orbiter(Sprite sprite, OrbiterBlueprint blueprint) {
        super(sprite);
        this.angularVelocity = blueprint.angularVelocity;
        this.zTilt = blueprint.tilt;
        this.xTilt = blueprint.xTilt;
        this.angle = blueprint.angle;
        this.radius = blueprint.radius;
    }

    @Override
    public void update(float delta) {
        angle += (angularVelocity * delta);
        angle %= 360;

        // Start object on the XZ plane
        Vector3 position = new Vector3(radius * MathUtils.cosDeg(angle), 0, radius * MathUtils.sinDeg(angle));

        // Create rotation matrices and rotate objects to their final positions
        Matrix3 rotZ = new Matrix3().setToRotation(zTilt);
        Matrix3 rotX = new Matrix3().setToRotation(Vector3.X, xTilt);

        position.mul(rotZ);
        position.mul(rotX);

        // Set sprite position and zCoord for ordering and rendering
        getSprite().setPosition(PlanetGenerator.CENTER_X - getSprite().getWidth()/2 + position.x, PlanetGenerator.CENTER_Y - getSprite().getHeight()/2 + position.y);
        setZCoord((int)position.z);
    }

    public static class OrbiterBlueprint {
        public float angularVelocity;
        public float tilt;
        public float xTilt;
        public float angle;
        public float radius;
    }
}
