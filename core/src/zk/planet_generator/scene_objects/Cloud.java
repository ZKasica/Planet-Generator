package zk.planet_generator.scene_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Cloud implements Json.Serializable {
    private Array<Orbiter> cloudObjects;
    private int color;

    private Cloud() {

    }

    public Cloud(Array<Orbiter> cloudObjects) {
        this.cloudObjects = cloudObjects;
        color = cloudObjects.first().getColor();
    }

    public Array<Orbiter> getCloudObjects() {
        return cloudObjects;
    }

    public float getAngularVelocity() {
        return cloudObjects.first().getAngularVelocity();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        for(Orbiter cloudObj : cloudObjects) {
            cloudObj.setColor(color);
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("cloudObjects", cloudObjects);

        // TODO: Clouds all orbit at the same xTilt and zTilt, find similar values and keep track of them instead of having them in every cloud json object
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        cloudObjects = json.readValue("cloudObjects", Array.class, new Array(), jsonData);
    }
}
