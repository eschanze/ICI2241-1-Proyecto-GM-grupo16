package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class PatronAtaque {
    // Atributos comunes a todos los patrones
    protected float tiempoTranscurrido;
    protected float duracion;
    protected float intervaloDisparo;
    protected float ultimoDisparo;
    protected Texture texturaBala;
    protected float origenX, origenY;
    protected int tipo;
    protected float velocidadBala;
    
    // Constructor base
    public PatronAtaque(float x, float y, int tipo, float velocidad, float duracion, float interDisparo, Texture tex) {
        this.origenX = x;
        this.origenY = y;
        this.tipo = tipo;
        this.velocidadBala = velocidad;
        this.duracion = duracion;
        this.texturaBala = tex;
        this.intervaloDisparo = interDisparo;
    }
    
    // Método común para iniciar
    public void iniciar() {
        tiempoTranscurrido = 0;
        ultimoDisparo = 0;
    }
    
    // Método común para establecer posición
    public void setPosition(float x, float y) {
        this.origenX = x;
        this.origenY = y;
    }
    
    // Método común para verificar si está completo
    public boolean estaCompleto() {
        return tiempoTranscurrido >= duracion;
    }
    
    // Método común para dibujar proyectiles
    public void dibujar(SpriteBatch batch, Array<Proyectil> proyectiles) {
        for (Proyectil p : proyectiles) {
            Rectangle pArea = p.getArea();
            batch.draw(p.textura, pArea.x, pArea.y);
        }
    }
    
    // Método común para limpiar (puede ser sobrescrito si es necesario)
    public void limpiar() {
        // Implementación base vacía
    }
    
    // Métodos abstractos que deben ser implementados por las subclases
    public abstract void actualizar(float delta, Array<Proyectil> proyectiles);
    public abstract PatronAtaque clone();
}