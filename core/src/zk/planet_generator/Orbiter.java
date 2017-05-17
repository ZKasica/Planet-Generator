package zk.planet_generator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Zach on 5/16/2017.
 */
public class Orbiter extends SpaceObject {
    private float angularVelocity;
    private float tilt;
    private float angle;
    private float radius;

    public Orbiter(Sprite sprite, float angularVelocity, float tilt, float angle, float radius) {
        super(sprite);
        this.angularVelocity = angularVelocity;
        this.tilt = tilt;
        this.angle = angle;
        this.radius = radius;
    }

    @Override
    public void update(float delta) {
        angle += (angularVelocity * delta);
        angle %= 360;

        float x = radius * MathUtils.cosDeg(angle);
        float y = 0;

        x = x*MathUtils.cosDeg(tilt) - y*MathUtils.sinDeg(tilt);
        y = x*MathUtils.sinDeg(tilt) + y*MathUtils.cosDeg(tilt);
        getSprite().setPosition(PlanetGenerator.CENTER_X - getSprite().getWidth()/2 + x, PlanetGenerator.CENTER_Y - getSprite().getHeight()/2 + y);

        setZCoord((int) (radius * MathUtils.sinDeg(angle)));
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getTilt() {
        return tilt;
    }

    public void setTilt(float tilt) {
        this.tilt = tilt;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
