package puppy.code;

/**
 * Patrón Strategy (GM2.3)
 * Esta interfaz define la estrategia para manejar las colisiones de proyectiles.
 * Permite cambiar el comportamiento de colisión de forma dinámica sin modificar
 * la clase Proyectil.
 */
public interface EstrategiaColision {
    /**
     * Define el comportamiento al colisionar con el jugador.
     * 
     * @param jugador El jugador con el que se colisiona.
     * @return true si el proyectil debe ser destruido, false en caso contrario.
     */
    boolean alColisionar(Jugador jugador);
}