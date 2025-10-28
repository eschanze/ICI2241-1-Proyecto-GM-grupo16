package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public interface PatronAtaque {
    void iniciar();
    void setPosition(float x, float y);
    void actualizar(float delta, Array<Proyectil> proyectiles);
    boolean estaCompleto();
    void dibujar(SpriteBatch batch, Array<Proyectil> proyectiles);
    void limpiar();
}