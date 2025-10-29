package puppy.code;

public class PatronTimeline {
    private PatronAtaque patron;
    private float startTime;
    private float endTime;
    private boolean canOverlap; // Indica si este patrón puede solaparse con otros patrones activos

    public PatronTimeline(PatronAtaque patron, float x, float y, float startTime, float endTime, boolean canOverlap) {
        this.patron = patron.clone();  // Crear una copia del patrón
        this.patron.setPosition(x, y); // Establecer la posición en la copia
        this.startTime = startTime;
        this.endTime = endTime;
        this.canOverlap = canOverlap;
    }

    public PatronAtaque getPatron() {
        return patron;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }
}