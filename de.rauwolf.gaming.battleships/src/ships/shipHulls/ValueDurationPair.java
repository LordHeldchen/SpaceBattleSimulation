package ships.shipHulls;

public class ValueDurationPair {
    private final Integer value;
    private final Integer duration;

    public ValueDurationPair(Integer value, Integer duration) {
        this.value = value;
        this.duration = duration;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getDuration() {
        return duration;
    }
}
