package zk.planet_generator.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import zk.planet_generator.PlanetGeneratorGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(1280, 720);
                return config;
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new PlanetGeneratorGame();
        }
}