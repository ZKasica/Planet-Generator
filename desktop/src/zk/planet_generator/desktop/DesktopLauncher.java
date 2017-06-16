package zk.planet_generator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import zk.planet_generator.PlanetGeneratorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.fullscreen = false;
		config.title = "Planet Generator";
		new LwjglApplication(new PlanetGeneratorGame(), config);
	}
}
