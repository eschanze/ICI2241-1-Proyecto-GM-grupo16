package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PatronCircular extends PatronAtaque {
    private int numBalas;
    private float anguloOffset;
    private float velocidadRotacionOffset;

    public PatronCircular(float x, float y, int tipo, int numBalas, float velocidad, float duracion, float interDisparo,
            float velRotation, Texture tex) {
        super(x, y, tipo, velocidad, duracion, interDisparo, tex);
        this.numBalas = numBalas;
        this.velocidadRotacionOffset = velRotation;
        this.anguloOffset = 0;
    }

    @Override
    public void actualizar(float delta, Array<Proyectil> proyectiles) {
        tiempoTranscurrido += delta;
        ultimoDisparo += delta;

        // Rotar el patrón con el tiempo
        anguloOffset += velocidadRotacionOffset * delta;

        if (ultimoDisparo >= intervaloDisparo) {
            dispararCirculo(proyectiles);
            ultimoDisparo = 0;
        }
    }

    private void dispararCirculo(Array<Proyectil> proyectiles) {
        float anguloIncremento = 360f / numBalas;

        for (int i = 0; i < numBalas; i++) {
            float angulo = (i * anguloIncremento + anguloOffset) * MathUtils.degreesToRadians;
            float vx = MathUtils.cos(angulo) * velocidadBala;
            float vy = MathUtils.sin(angulo) * velocidadBala;

            proyectiles.add(new Proyectil(origenX, origenY, vx, vy, tipo, texturaBala));
        }
    }

    @Override
    public PatronAtaque clone() {
        return new PatronCircular(origenX, origenY, tipo, numBalas, velocidadBala, duracion, intervaloDisparo,
                velocidadRotacionOffset, texturaBala);
    }
}