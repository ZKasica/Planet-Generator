package zk.planet_generator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import zk.planet_generator.PlanetGenerator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.title = "Planet Generator";
		config.fullscreen = true;
		new LwjglApplication(new PlanetGenerator(), config);
	}
}
