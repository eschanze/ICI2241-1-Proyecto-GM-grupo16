package puppy.code;

import com.badlogic.gdx.math.Rectangle;

// Interfaz para objetos que pueden colisionar con otros (Por ejemplo, el jugador, Proyectil.java, etc.)
// Es una propiedad común entre clases que no tienen una relación de herencia directa, por lo que una interfaz tiene sentido y cumple
// con el requisito GM1.5
// Permite que los objetos sean responsables de manejar sus propias colisiones y lo que sucede cuando ocurre una colisión. 
// Por lo tanto se puede mover esa lógica desde Lluvia.java (donde estaba originalmente en el juego ejemplo).
public interface Colisionable {

    // Devuelve el área de colisión del objeto
    Rectangle getHitbox();

    // Verifica si este objeto colisiona con otro Colisionable
    default boolean colisionaCon(Colisionable other) {
        return this.getHitbox().overlaps(other.getHitbox());
    }

    // Acción a realizar al colisionar con otro objeto Colisionable
    // Retorna un boolean que representa si se debería "consumir" (remover) el
    // objeto al colisionar
    boolean alColisionar(Colisionable other);
}