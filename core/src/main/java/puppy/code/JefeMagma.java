package puppy.code;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.PatronCircular;
import puppy.code.PatronEspiral;

/**
 * Jefe del nivel de Magma.
 * Producto concreto del patrón Abstract Factory (GM2.4).
 * 
 * Crea patrones de ataque con temática de fuego/magma,
 * utilizando las texturas específicas del nivel.
 */
public class JefeMagma extends Jefe {
    
    // Constructor del Jefe de Magma. Recibe las texturas de los proyectiles del nivel como parámetros.
    public JefeMagma(Texture texturaPrimaria, Texture texturaSecundaria) {
        super(texturaPrimaria, texturaSecundaria);
    }
    
    @Override
    public PatronCircular crearPatronCircular(
        float x, float y, int tipo, int numBalas, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    ) {
        // Crear patrón circular con la textura primaria (temática de magma)
        return new PatronCircular(x, y, tipo, numBalas, velocidad, duracion, 
                                  interDisparo, velRotacion, texturaBalasPrimaria);
    }
    
    @Override
    public PatronEspiral crearPatronEspiral(
        float x, float y, int tipo, int numBrazos, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    ) {
        // Crear patrón espiral con la textura secundaria (temática de magma)
        return new PatronEspiral(x, y, tipo, numBrazos, velocidad, duracion, 
                                 interDisparo, velRotacion, texturaBalasSecundaria);
    }
    
    @Override
    public String getNombre() {
        return "Dragón de Magma";
    }
}