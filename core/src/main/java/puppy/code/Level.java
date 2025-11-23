package puppy.code;

import com.badlogic.gdx.utils.Array;

public class Level {
    private String levelName;
    private int levelNumber;
    private Array<PatronTimeline> patronTimelines = new Array<PatronTimeline>();
    private float totalDuration; // en segundos
    private Jefe jefe; // Jefe del nivel

    public Array<PatronTimeline> getPatronSequence() {
        return patronTimelines;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public float getTotalDuration() {
        return totalDuration;
    }

    public void setLevelName(String name) {
        this.levelName = name;
    }

    public void setLevelNumber(int number) {
        this.levelNumber = number;
    }

    public void addPatron(PatronTimeline patronTimeline) {
        patronTimelines.add(patronTimeline);
    }

    public Jefe getJefe() {
        return jefe;
    }

    public void setJefe(Jefe jefe) {
        this.jefe = jefe;
    }
}