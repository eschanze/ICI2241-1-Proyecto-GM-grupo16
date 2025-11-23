package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class MagmaLevelFactory extends LevelEnemyFactory {
    
    // Constructor de la fábrica de nivel de Magma.
    public MagmaLevelFactory(Texture texturaPrimaria, Texture texturaSecundaria) {
        super(texturaPrimaria, texturaSecundaria);
    }
    
    @Override
    public Jefe crearJefe() {
        // Crear y retornar un jefe de magma con las texturas del nivel
        return new JefeMagma(texturaBalasPrimaria, texturaBalasSecundaria);
    }
}