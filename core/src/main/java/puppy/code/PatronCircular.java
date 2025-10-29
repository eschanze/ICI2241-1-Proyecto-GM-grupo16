package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PatronCircular implements PatronAtaque {
    // Parámetros del patrón de ataque
    private float tiempoTranscurrido;
    private float duracion;
    private float intervaloDisparo;
    private float ultimoDisparo;
    private Texture texturaBala;
    private float origenX, origenY;
    private int tipo;
    private int numBalas;
    private float velocidadBala;
    private float anguloOffset;
    private float velocidadRotacionOffeset;
    
    public PatronCircular(float x, float y, int tipo, int numBalas, float velocidad, float duracion, float interDisparo, float velRotation, Texture tex) {
        this.origenX = x;
        this.origenY = y;
        this.tipo = tipo; // Tipo de proyectil (1 = dañino, etc.)
        this.numBalas = numBalas;
        this.velocidadBala = velocidad;
        this.duracion = duracion;
        this.texturaBala = tex;
        this.intervaloDisparo = interDisparo; // Disparar cada x segundos
        this.velocidadRotacionOffeset = velRotation;
        this.anguloOffset = 0;
    }
    
    @Override
    public void iniciar() {
        tiempoTranscurrido = 0;
        ultimoDisparo = 0;
    }

    @Override
    public void setPosition(float x, float y) {
        this.origenX = x;
        this.origenY = y;
    }
    
    @Override
    public void actualizar(float delta, Array<Proyectil> proyectiles) {
        tiempoTranscurrido += delta;
        ultimoDisparo += delta;

        // Rotar el patrón con el tiempo
        anguloOffset += velocidadRotacionOffeset * delta;
        
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
    public boolean estaCompleto() {
        return tiempoTranscurrido >= duracion;
    }
    
    @Override
    public void dibujar(SpriteBatch batch, Array<Proyectil> proyectiles) {
        for (Proyectil p : proyectiles) {
            Rectangle pArea = p.getArea();
            batch.draw(p.textura, pArea.x, pArea.y);
        }
    }
    
    @Override
    public void limpiar() {
        // No hay recursos específicos que limpiar en este patrón
    }

    @Override
    public PatronAtaque clone() {
        return new PatronCircular(origenX, origenY, tipo, numBalas, velocidadBala, duracion, intervaloDisparo, velocidadRotacionOffeset, texturaBala);
    }
}