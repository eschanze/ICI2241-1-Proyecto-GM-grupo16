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

        /**
         * Patrón Abstract Factory (GM2.4)
         * 
         * Usamos fábricas de nivel para crear familias de objetos relacionados
         * (jefes y patrones de ataque) específicos de cada nivel temático.
         * 
         * Cada LevelEnemyFactory concreta (MagmaLevelFactory, IceLevelFactory)
         * crea productos (Jefe) que son coherentes con su tema.
         */

        // Crear fábrica para el nivel de Magma
        // gota y gota2 son las texturas de los proyectiles del nivel de magma
        // El nivel de magma es el único jugable por ahora...
        LevelEnemyFactory magmaFactory = new MagmaLevelFactory(gota, gota2);
        
        /**
         * Nivel 1: Dragón de Magma
         * Duración total: 60 segundos
         **/
        Level level1 = new Level();
        
        // Usar la fábrica para crear el jefe del nivel
        Jefe jefeMagma = magmaFactory.crearJefe();
        level1.setJefe(jefeMagma);
        level1.setLevelName(jefeMagma.getNombre()); // Usar el nombre del jefe
        level1.setLevelNumber(1);
        
        // Crear patrones de ataque usando el jefe (que conoce las texturas del nivel)
        PatronCircular patron_circular_1 = jefeMagma.crearPatronCircular(0, 0, 1, 12, 150f, 60f, 1, 20f);
        PatronCircular patron_circular_2 = jefeMagma.crearPatronCircular(0, 0, 1, 24, 300f, 20f, 1, 20f);
        PatronCircular patron_circular_mini = jefeMagma.crearPatronCircular(0, 0, 1, 24, 300f, 5f, 1, 20f);
        PatronEspiral patron_espiral_1 = jefeMagma.crearPatronEspiral(0, 0, 1, 8, 50f, 15f, 1f, 60f);
        
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