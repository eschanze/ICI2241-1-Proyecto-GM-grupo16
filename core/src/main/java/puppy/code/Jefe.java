package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/**
 * Clase abstracta que representa un jefe (boss) del nivel.
 * Esta clase es parte del patrón Abstract Factory (GM2.4).
 * Define la interfaz común para todos los jefes del juego.
 * 
 * Cada jefe tiene la capacidad de crear patrones de ataque específicos
 * temáticos para su nivel que usan distintas texturas (rojas para el de magma, azules para el de hielo).
 */
public abstract class Jefe {
    // Texturas de las balas del nivel
    protected Texture texturaBalasPrimaria;
    protected Texture texturaBalasSecundaria;
    
    // Constructor base para todos los jefes.
    public Jefe(Texture texturaPrimaria, Texture texturaSecundaria) {
        this.texturaBalasPrimaria = texturaPrimaria;
        this.texturaBalasSecundaria = texturaSecundaria;
    }
    
    // Crea un patrón de ataque circular temático para este jefe.
    public abstract PatronCircular crearPatronCircular(
        float x, float y, int tipo, int numBalas, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    );
    
    // Crea un patrón de ataque en espiral temático para este jefe.
    public abstract PatronEspiral crearPatronEspiral(
        float x, float y, int tipo, int numBrazos, 
        float velocidad, float duracion, float interDisparo, float velRotacion
    );
    
    // Getter para el nombre del jefe.
    public abstract String getNombre();
}