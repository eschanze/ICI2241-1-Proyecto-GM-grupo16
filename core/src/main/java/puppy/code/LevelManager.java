package puppy.code;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

public class LevelManager {
    private Array<Level> levels = new Array<Level>();
    private int currentLevelIndex;
    private boolean loaded = false; // Indica si los niveles han sido cargados

    public void loadLevels(Texture gota) {
        // Nivel 1 de prueba, con 3 patrones circulares
        // Duración total: 10 segundos
        Level level1 = new Level();
        level1.setLevelName("Boss 1");
        level1.setLevelNumber(1);
        PatronCircular patron1 = new PatronCircular(0, 0, 1, 12, 150f, 10f, 1, 20, gota);
        PatronCircular patron2 = new PatronCircular(0, 0, 1, 24, 300f, 10f, 1, 20, gota);
        //level1.addPatron(new PatronTimeline(patron1, 200, 400, 0f, 10f, true));
        //level1.addPatron(new PatronTimeline(patron1, 600, 400, 0f, 10f, true));
        level1.addPatron(new PatronTimeline(patron2, 400, 400, 3f, 6f, true));
        level1.addPatron(new PatronTimeline(patron2, 200, 400, 6f, 9f, true));
        //level1.addPatron(new PatronTimeline(patron2, 600, 400, 6f, 9f, true));
        levels.add(level1);

        loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Level getCurrentLevel() {
        if (currentLevelIndex < levels.size) {
            return levels.get(currentLevelIndex);
        } else {
            return null; // No hay más niveles
        }
    }

    public Level getNextLevel() {
        currentLevelIndex++;
        return getCurrentLevel();
    }

    public boolean hasNextLevel() {
        return currentLevelIndex + 1 < levels.size;
    }

    public void advanceLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
        }
    }

    public void reset() {
        currentLevelIndex = 0;
    }
}