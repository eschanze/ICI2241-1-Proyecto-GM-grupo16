package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/**
 * Jefe del nivel de Hielo.
 * Producto concreto del patrón Abstract Factory (GM2.4).
 * 
 * Crea patrones de ataque con temática de hielo,
 * utilizando las texturas específicas del nivel.
 */
public class JefeHielo extends Jefe {
    
    // Constructor del Jefe de Hielo. Recibe las texturas de los proyectiles del nivel como parámetros.
    public JefeHielo(Texture texturaPrimaria, Texture texturaSecundaria) {
        super(texturaPrimaria, texturaSecundaria);
    }
    
    @Override
    public PatronCircular crearPatronCircular(
        float x, float y, int tipo, int numBalas, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    ) {
        // Crear patrón circular con la textura primaria (temática de hielo)
        return new PatronCircular(x, y, tipo, numBalas, velocidad, duracion, 
                                  interDisparo, velRotacion, texturaBalasPrimaria);
    }
    
    @Override
    public PatronEspiral crearPatronEspiral(
        float x, float y, int tipo, int numBrazos, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    ) {
        // Crear patrón espiral con la textura secundaria (temática de hielo)
        return new PatronEspiral(x, y, tipo, numBrazos, velocidad, duracion, 
                                 interDisparo, velRotacion, texturaBalasSecundaria);
    }
    
    @Override
    public String getNombre() {
        return "Golem de Hielo";
    }
}