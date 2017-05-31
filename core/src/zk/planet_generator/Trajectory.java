package zk.planet_generator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.xpath.internal.operations.Or;
import zk.planet_generator.scene_objects.Orbiter;
import zk.planet_generator.scene_objects.Ring;

/**
 * Created by Zach on 5/30/2017.
 */
public class Trajectory {
    private Orbiter orbiter;
    private Ring path;

    public Trajectory(Orbiter orbiter) {
        this.orbiter = orbiter;
        int degreeIncrement = 10;
        Array<Orbiter> pathObjects = new Array<Orbiter>();
        for(int i = 0; i < 360; i += degreeIncrement) {
            Orbiter.OrbiterBlueprint orbiterBlueprint = new Orbiter.OrbiterBlueprint();
            orbiterBlueprint.angle = i;
            orbiterBlueprint.angularVelocity = 10;
            orbiterBlueprint.radius = orbiter.getRadius();
            orbiterBlueprint.xTilt = orbiter.getXTilt();
            orbiterBlueprint.zTilt = orbiter.getZTilt();

            Sprite trajectoryDot = new Sprite(Scene.pixelTexture);
            Color color = new Color();
            Color.rgba8888ToColor(color, orbiter.getColor());
            trajectoryDot.setColor(color);
            trajectoryDot.setSize(2, 2);

            pathObjects.add(new Orbiter(trajectoryDot, orbiterBlueprint));
        }
        path = new Ring(pathObjects);
    }

    public void update() {
        path.setXTilt(orbiter.getXTilt());
        path.setZTilt(orbiter.getZTilt());
        path.setRadius(orbiter.getRadius());
    }

    public Ring getPath() {
        return path;
    }
}
