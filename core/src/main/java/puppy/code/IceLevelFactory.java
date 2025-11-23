package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class IceLevelFactory extends LevelEnemyFactory {
    
    // Constructor de la fábrica de nivel de Hielo.
    public IceLevelFactory(Texture texturaPrimaria, Texture texturaSecundaria) {
        super(texturaPrimaria, texturaSecundaria);
    }
    
    @Override
    public Jefe crearJefe() {
        // Crear y retornar un jefe de hielo con las texturas del nivel
        return new JefeHielo(texturaBalasPrimaria, texturaBalasSecundaria);
    }
}