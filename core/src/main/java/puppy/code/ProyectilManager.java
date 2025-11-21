package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ProyectilManager {
    // Variables del nivel
    private Array<Proyectil> proyectiles;
    private Level currentLevel;
    private float levelTime;
    private Array<PatronAtaque> activePatterns; // Patrones activos en el nivel, permite múltiples patrones simultáneos
    private Array<PatronTimeline> pendingPatterns;
    private boolean levelComplete;

    // Variables de texturas y sonidos
    Texture bulletTex;
    Music backgroundMusic;

    // Sprite del dragón (no hace nada todavía, pero es progreso para la entrega
    // final)
    private Texture magmaDragonTex;

    // Tiempo para sumar puntos automáticamente
    private long ultimoTiempoPuntos;

    public ProyectilManager(Texture bulletTex, Music backgroundMusic) {
        // Inicializar sonidos
        this.backgroundMusic = backgroundMusic;
        // Inicializar texturas
        this.bulletTex = bulletTex;
        // Inicializar arrays
        this.proyectiles = new Array<Proyectil>();
        this.activePatterns = new Array<PatronAtaque>();
        this.pendingPatterns = new Array<PatronTimeline>();
        // Cargar textura del dragón
        magmaDragonTex = new Texture(Gdx.files.internal("magma_dragon_256.png"));
    }

    // Al inicial el juego, se llama a esta función
    public void crear(Level level) {
        // Inicializar variables
        this.currentLevel = level;
        this.levelTime = 0;
        this.activePatterns.clear();
        this.proyectiles.clear();
        this.pendingPatterns = new Array<>(level.getPatronSequence());
        this.levelComplete = false;

        ultimoTiempoPuntos = TimeUtils.millis(); // Iniciar el temporizador de puntos

        // Empezar la reproducción de la música de fondo inmediatamente
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    // Todos los frames se llama a esta función para actualizar la lógica de los
    // proyectiles
    public boolean actualizarMovimiento(Jugador jugador) {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        levelTime += delta;

        // Activar los patrones en base a la línea de tiempo del nivel
        for (int i = pendingPatterns.size - 1; i >= 0; i--) {
            PatronTimeline pt = pendingPatterns.get(i);
            if (levelTime >= pt.getStartTime()) {
                PatronAtaque patron = pt.getPatron();
                patron.iniciar();
                activePatterns.add(patron);
                pendingPatterns.removeIndex(i);
            }
        }

        // Actualizar patrones activos
        for (int i = activePatterns.size - 1; i >= 0; i--) {
            PatronAtaque patron = activePatterns.get(i);
            patron.actualizar(delta, proyectiles);

            if (patron.estaCompleto()) {
                patron.limpiar();
                activePatterns.removeIndex(i);
            }
        }

        // Verificar si el nivel está completo (para el avance está hardcodeado el
        // "fin")
        if ((pendingPatterns.size == 0 && activePatterns.size == 0 && proyectiles.size == 0) || (levelTime >= 65f)) {
            levelComplete = true;
        }

        // Actualizar proyectiles y verificar colisiones con el tarro
        for (int i = proyectiles.size - 1; i >= 0; i--) {
            Proyectil p = proyectiles.get(i);
            p.actualizar(delta);

            // Remover si está fuera de pantalla
            if (p.fueraDePantalla()) {
                proyectiles.removeIndex(i);
                continue;
            }

            // Verificar colisión con el tarro (Proyectil.hitbox vs Jugador.hitbox)
            if (jugador.colisionaCon(p)) {
                jugador.alColisionar(p);

                // Revisar si se debería eliminar el proyectil
                if (p.alColisionar(jugador)) {
                    proyectiles.removeIndex(i);
                }

                // Revisar si el jugador murió después de la colisión
                if (jugador.getVidas() <= 0)
                    return false;
            }
        }

        // Cada 5 segundos, sumar 50 puntos automáticamente
        if (TimeUtils.timeSinceMillis(ultimoTiempoPuntos) > 5000) {
            jugador.sumarPuntos(50);
            ultimoTiempoPuntos = TimeUtils.millis();
        }

        // Retorna true o false dependiendo si el jugador sigue vivo
        return true;
    }

    public void actualizarDibujoProyectiles(SpriteBatch batch) {
        // Dibujar todos los proyectiles de los patrones activos
        for (PatronAtaque patron : activePatterns) {
            patron.dibujar(batch, proyectiles);
        }
        // Dibujar el dragón en una posición fija, por ahora
        if (magmaDragonTex != null) {
            float x = 280f;
            float y = 235f;
            batch.draw(magmaDragonTex, x, y);
        }
    }

    public void destruir() {
        // dropSound.dispose();
        backgroundMusic.dispose();
    }

    public void pausar() {
        backgroundMusic.pause();
    }

    public void continuar() {
        backgroundMusic.play();
    }

    // Indica si el nivel actual ha terminado (sin patrones pendientes, sin
    // proyectiles activos)
    public boolean isLevelComplete() {
        return levelComplete;
    }
}