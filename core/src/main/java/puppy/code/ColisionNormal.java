package puppy.code;

public class ColisionNormal implements EstrategiaColision {
    @Override
    public boolean alColisionar(Jugador jugador) {
        jugador.dañar();
        return true;
    }
}