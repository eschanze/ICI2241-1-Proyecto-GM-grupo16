package puppy.code;

import com.badlogic.gdx.utils.Array;

public class Level {
    private String levelName;
    private int levelNumber;
    private Array<PatronTimeline> patronTimelines = new Array<PatronTimeline>();
    private float totalDuration; // en segundos

    public Array<PatronTimeline> getPatronSequence() {
        return patronTimelines;
    }

    public void setLevelName(String name) {
        this.levelName = name;
    }

    public void setLevelNumber (int number) {
        this.levelNumber = number;
    }

    public void addPatron(PatronTimeline patronTimeline) {
        patronTimelines.add(patronTimeline);
    }
}
