package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PatronEspiral extends PatronAtaque {
    private int numBrazos;
    private float anguloActual;
    private float velocidadRotacion;
    private float anguloIncrementoPorDisparo;

    public PatronEspiral(float x, float y, int tipo, int numBrazos, float velocidad, float duracion, float interDisparo, float velRotacion, Texture tex) {
        super(x, y, tipo, velocidad, duracion, interDisparo, tex);
        this.numBrazos = numBrazos;
        this.velocidadRotacion = velRotacion;
        this.anguloActual = 0;
        this.anguloIncrementoPorDisparo = 15f; // Incremento de ángulo en cada disparo para crear el efecto espiral
    }

    @Override
    public void actualizar(float delta, Array<Proyectil> proyectiles) {
        tiempoTranscurrido += delta;
        ultimoDisparo += delta;
        
        // Rotar el patrón base
        anguloActual += velocidadRotacion * delta;
        
        if (ultimoDisparo >= intervaloDisparo) {
            dispararEspiral(proyectiles);
            ultimoDisparo = 0;
        }
    }

    private void dispararEspiral(Array<Proyectil> proyectiles) {
        float anguloEntreBrazos = 360f / numBrazos;
        
        for (int i = 0; i < numBrazos; i++) {
            // Calcular ángulo de cada brazo, rotando con el tiempo y agregando el offset espiral
            float angulo = (anguloActual + (i * anguloEntreBrazos)) * MathUtils.degreesToRadians;
            float vx = MathUtils.cos(angulo) * velocidadBala;
            float vy = MathUtils.sin(angulo) * velocidadBala;
            
            proyectiles.add(new Proyectil(origenX, origenY, vx, vy, tipo, texturaBala));
        }
        
        // Incrementar el ángulo para crear el efecto espiral
        anguloActual += anguloIncrementoPorDisparo;
    }

    @Override
    public PatronAtaque clone() {
        return new PatronEspiral(origenX, origenY, tipo, numBrazos, velocidadBala, duracion, intervaloDisparo, velocidadRotacion, texturaBala);
    }
}