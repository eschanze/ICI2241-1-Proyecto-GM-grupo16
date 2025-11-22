package puppy.code;

public class ColisionCurativa implements EstrategiaColision {
    @Override
    public boolean alColisionar(Jugador jugador) {
        jugador.sumarPuntos(10);
        return true;
    }
}