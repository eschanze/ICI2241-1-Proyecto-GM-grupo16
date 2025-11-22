package puppy.code;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

/**
 * LevelManager utiliza el patrón Singleton (GM2.1)
 * Garantiza una única instancia del gestor de niveles en toda la aplicación
 * y proporciona un punto de acceso global a ella.
 */
public class LevelManager {
    // Instancia única del Singleton
    private static LevelManager instance;

    private Array<Level> levels = new Array<Level>();
    private int currentLevelIndex;
    private boolean loaded = false; // Indica si los niveles han sido cargados

    private LevelManager() {
        // Constructor privado para evitar instanciación externa
    }

    // Método de acceso global a la instancia única
    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void loadLevels(Texture gota, Texture gota2) {

        // Crear patrones comunes para todos los niveles
        PatronCircular patron_circular_1 = new PatronCircular(0, 0, 1, 12, 150f, 60f, 1, 20f, gota);
        PatronCircular patron_circular_2 = new PatronCircular(0, 0, 1, 24, 300f, 20f, 1, 20f, gota);
        PatronCircular patron_circular_mini = new PatronCircular(0, 0, 1, 24, 300f, 5f, 1, 20f, gota);
        PatronEspiral patron_espiral_1 = new PatronEspiral(0, 0, 1, 8, 50f, 15f, 1f, 60f, gota2);

        /**
         * Nivel 1 de prueba, para el avance del proyecto
         * Nombre: Dragón de Magma
         * Duración total: 60 segundos
         **/
        Level level1 = new Level();
        level1.setLevelName("Dragón de Magma");
        level1.setLevelNumber(1);
        // Línea de tiempo del nivel 1
        level1.addPatron(new PatronTimeline(patron_circular_1, 392, 300, 1f, 1f, true));
        level1.addPatron(new PatronTimeline(patron_circular_2, 392, 300, 10f, 1f, true));
        level1.addPatron(new PatronTimeline(patron_espiral_1, 50, 300, 30f, 1f, true));
        level1.addPatron(new PatronTimeline(patron_circular_mini, 392, 300, 42f, 1f, true));
        level1.addPatron(new PatronTimeline(patron_espiral_1, 700, 300, 45f, 1f, true));
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
            return null; // Por ahora, no hay más niveles
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