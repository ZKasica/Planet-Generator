package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import zk.planet_generator.Scene;

public class Trajectory {
    private Orbiter orbiter;
    private Ring path;
    private int speed;

    public Trajectory(Orbiter orbiter) {
        this.orbiter = orbiter;
        int degreeIncrement = 10;
        speed = 10;

        Array<Orbiter> pathObjects = new Array<Orbiter>();
        for(int i = 0; i < 360; i += degreeIncrement) {
            Orbiter.OrbiterBlueprint orbiterBlueprint = new Orbiter.OrbiterBlueprint();
            orbiterBlueprint.angle = i;
            orbiterBlueprint.angularVelocity = speed;
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

        if(orbiter.getAngularVelocity() != 0) {
            int angularVel = (int) (orbiter.getAngularVelocity() / Math.abs(orbiter.getAngularVelocity())) * speed;
            path.setAngularVelocity(angularVel);
        }
    }

    public Ring getPath() {
        return path;
    }
}
