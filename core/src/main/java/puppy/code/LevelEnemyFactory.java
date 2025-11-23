package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/**
 * Fábrica abstracta para crear enemigos y componentes de nivel.
 * Implementa el patrón Abstract Factory (GM2.4).
 * 
 * Define la interfaz para crear familias de objetos relacionados
 * (jefes, patrones de ataque) sin especificar sus clases concretas.
 * 
 * Cada nivel temático (Magma, Hielo, etc.) tiene su propia fábrica concreta
 * que crea jefes y componentes cohesivos con la temática del nivel.
 */
public abstract class LevelEnemyFactory {
    // Texturas de las balas para este nivel
    protected Texture texturaBalasPrimaria;
    protected Texture texturaBalasSecundaria;
    
    // Constructor base para las fábricas de nivel.
    public LevelEnemyFactory(Texture texturaPrimaria, Texture texturaSecundaria) {
        this.texturaBalasPrimaria = texturaPrimaria;
        this.texturaBalasSecundaria = texturaSecundaria;
    }
    
    /**
     * Crea el jefe (boss) del nivel.
     * Cada fábrica concreta retorna su propio tipo de jefe.
     */
    public abstract Jefe crearJefe();
}
