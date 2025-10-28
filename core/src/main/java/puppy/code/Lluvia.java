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
    // Variables patrón de ataque
    private Array<Proyectil> proyectiles;
    private Array<PatronAtaque> patrones;
    private PatronAtaque patronActual;
    private int indicePatron;
    // Variables de gotas de lluvia
    private Texture gotaBuena;
    private Texture gotaMala;
    private Sound dropSound;
    private Music rainMusic;

    // Zona permitida para las gotas
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    // Tiempo para sumar puntos automáticamente
    private long ultimoTiempoPuntos;
    
    public Lluvia(Texture gotaBuena, Texture gotaMala, Sound ss, Music mm) {
        // Inicializar sonidos
        rainMusic = mm;
        dropSound = ss;
        // Inicializar texturas
        this.gotaBuena = gotaBuena;
        this.gotaMala = gotaMala;
        // Definir zona de generación de gotas
        this.minX = 200;
        this.maxX = 600;
        this.minY = 400;
        this.maxY = 400;
    }

    // Genera una posición aleatoria dentro del área permitida
    private float[] obtenerPosicionAleatoria() {
        float x = MathUtils.random(minX, maxX);
        float y = MathUtils.random(minY, maxY);
        return new float[]{x, y};
    }
    
    public void crear() {
        proyectiles = new Array<Proyectil>();
        patrones = new Array<PatronAtaque>();

        // Por ahora, solo se generan gotas de lluvia con un patrón circular simple, con variación en la posición
        float[] pos = obtenerPosicionAleatoria();
        patrones.add(new PatronCircular(pos[0], pos[1], 32, 150, 5f, 0.5f, 30f, gotaBuena));
        
        // Iniciar el primer patrón
        indicePatron = 0;
        patronActual = patrones.get(indicePatron);
        patronActual.iniciar();
        ultimoTiempoPuntos = TimeUtils.millis(); // Iniciar el temporizador de puntos

		// Empezar la reproducción de la música de fondo inmediatamente
        rainMusic.setLooping(true);
        rainMusic.play();
    }
    
    public boolean actualizarMovimiento(Tarro tarro) {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        
        // Actualizar patrón actual
        patronActual.actualizar(delta, proyectiles);
        
        // Si el patrón actual ha terminado, pasar al siguiente
        if (patronActual.estaCompleto()) {
            patronActual.limpiar();
            indicePatron = (indicePatron + 1) % patrones.size;
            patronActual = patrones.get(indicePatron);

            // Antes de iniciar el nuevo patrón, actualizar su posición
            float[] pos = obtenerPosicionAleatoria();
            // No necesitamos verificar el tipo de patrón, ya que todos deben implementar setPosition
            patronActual.setPosition(pos[0], pos[1]);

            patronActual.iniciar();
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
            if (pHitbox.overlaps(tarro.getHitbox())) {
                if (p.tipo == 1) { // Dañina
                    tarro.dañar();
                    if (tarro.getVidas() <= 0)
                        return false;
                    proyectiles.removeIndex(i);
                } else { // Buena
                    tarro.sumarPuntos(10);
                    dropSound.play();
                    proyectiles.removeIndex(i);
                }
            }
        }

        // Cada 5 segundos, sumar 50 puntos automáticamente
        if (TimeUtils.timeSinceMillis(ultimoTiempoPuntos) > 5000) {
            tarro.sumarPuntos(50);
            ultimoTiempoPuntos = TimeUtils.millis();
        }

        return true;
    }
    
    public void actualizarDibujoLluvia(SpriteBatch batch) {
        patronActual.dibujar(batch, proyectiles);
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