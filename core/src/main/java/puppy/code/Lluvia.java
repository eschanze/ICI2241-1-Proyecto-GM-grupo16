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

public class Lluvia {
    // Variables del nivel
    private Array<Proyectil> proyectiles;
    private Level currentLevel;
    private float levelTime;
    private Array<PatronAtaque> activePatterns; // Patrones activos en el nivel, permite múltiples patrones simultáneos
    private Array<PatronTimeline> pendingPatterns;
    private boolean levelComplete;

    // Variables de texturas y sonidos
    private Texture gotaBuena;
    private Texture gotaMala;
    private Sound dropSound;
    private Music rainMusic;

    // Tiempo para sumar puntos automáticamente
    private long ultimoTiempoPuntos;
    
    public Lluvia(Texture gotaBuena, Texture gotaMala, Sound ss, Music mm) {
        // Inicializar sonidos
        rainMusic = mm;
        dropSound = ss;
        // Inicializar texturas
        this.gotaBuena = gotaBuena;
        this.gotaMala = gotaMala;
        // Inicializar arrays
        proyectiles = new Array<Proyectil>();
        activePatterns = new Array<PatronAtaque>();
        pendingPatterns = new Array<PatronTimeline>();
    }
    
    // Al inicial el juego, se llama a esta función para crear la lluvia
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
        rainMusic.setLooping(true);
        rainMusic.play();
    }
    
    // Todos los frames se llama a esta función para actualizar la lógica de la lluvia
    public boolean actualizarMovimiento(Tarro tarro) {
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

        // Verificar si el nivel está completo
        if (pendingPatterns.size == 0 && activePatterns.size == 0 && proyectiles.size == 0) {
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
            
            // Verificar colisión con el tarro (Proyector.hitbox vs Tarro.hitbox)
            Rectangle pHitbox = p.getHitbox();
            if (!pHitbox.overlaps(tarro.getHitbox())) continue;

            switch (p.tipo) {
                // 1: Proyectil normal
                case 1:
                    tarro.dañar();
                    if (tarro.getVidas() <= 0) return false;
                    proyectiles.removeIndex(i);
                
                // 2: "quieto-daño": daña solo si el tarro está QUIETO
                case 2:
                    if (!tarro.enMovimiento()) {
                        tarro.dañar();
                        if (tarro.getVidas() <= 0) return false;
                        proyectiles.removeIndex(i);
                    }
                    break;

                // 3: "mov-daño": daña solo si el tarro está EN MOVIMIENTO
                case 3:
                    if (tarro.enMovimiento()) {
                        tarro.dañar();
                        if (tarro.getVidas() <= 0) return false;
                    }
                    break;
                
                // 4 (ejemplo): Proyectil "bueno"
                case 4:
                default:
                    tarro.sumarPuntos(10);
                    dropSound.play();
                    proyectiles.removeIndex(i);
                    break;
            }
        }

        // Cada 5 segundos, sumar 50 puntos automáticamente
        if (TimeUtils.timeSinceMillis(ultimoTiempoPuntos) > 5000) {
            tarro.sumarPuntos(50);
            ultimoTiempoPuntos = TimeUtils.millis();
        }

        // Retorna true o false dependiendo si el jugador sigue vivo
        return true;
    }
    
    public void actualizarDibujoLluvia(SpriteBatch batch) {
        /*for (Proyectil p : proyectiles) {
            batch.draw(p.textura, p.getArea().x, p.getArea().y);
        }*/
        for (PatronAtaque patron : activePatterns) {
            patron.dibujar(batch, proyectiles);
        }
    }
    
    public void destruir() {
        dropSound.dispose();
        rainMusic.dispose();
    }
    
    public void pausar() {
        rainMusic.stop();
    }
    
    public void continuar() {
        rainMusic.play();
    }
}