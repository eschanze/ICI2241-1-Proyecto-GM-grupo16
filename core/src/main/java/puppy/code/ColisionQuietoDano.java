package puppy.code;

public class ColisionQuietoDano implements EstrategiaColision {
    @Override
    public boolean alColisionar(Jugador jugador) {
        if (!jugador.enMovimiento()) {
            jugador.dañar();
            return true;
        }
        return !jugador.enMovimiento(); // Si no se mueve, se destruye (porque dañó). Si se mueve, ¿debe destruirse?
        // Revisando lógica original:
        // case 2: // Quieto-daño
        // return !Jugador.enMovimiento();
        // La lógica original retornaba !enMovimiento().
        // Si está quieto (!enMovimiento() es true), retorna true (se destruye) y daña.
        // Si se mueve (enMovimiento() es true), retorna false (no se destruye) y no
        // daña.
    }
}