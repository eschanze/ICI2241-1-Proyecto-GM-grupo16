package puppy.code;

public class ColisionMovimientoDano implements EstrategiaColision {
    @Override
    public boolean alColisionar(Jugador jugador) {
        if (jugador.enMovimiento()) {
            jugador.dañar();
            return true;
        }
        return jugador.enMovimiento();
        // Lógica original:
        // case 3: // Mov-daño
        // return Jugador.enMovimiento();
        // Si se mueve, retorna true (se destruye) y daña.
        // Si está quieto, retorna false (no se destruye) y no daña.
    }
}