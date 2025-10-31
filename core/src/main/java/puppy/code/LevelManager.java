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
        // Crear patrones
        PatronCircular patron_circular_1 = new PatronCircular(0, 0, 1, 12, 150f, 5f, 1, 20f, gota);
        //PatronCircular patron_circular_2 = new PatronCircular(0, 0, 1, 24, 300f, 5f, 1, 20f, gota);
        //PatronEspiral patron_espiral_1 = new PatronEspiral(0, 0, 1, 16, 30f, 24f, 0.5f, 30f, gota);
        //PatronEspiral patron_espiral_2 = new PatronEspiral(0, 0, 1, 8, 200f, 24f, 0.1f, 30f, gota);
        // Línea de tiempo del nivel 1
        level1.addPatron(new PatronTimeline(patron_circular_1, 200, 400, 3.5f, 3.5f, true));
        level1.addPatron(new PatronTimeline(patron_circular_1, 400, 400, 4.5f, 5.5f, true));
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