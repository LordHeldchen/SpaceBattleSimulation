package ships.layouts;

public class ElectronicsLayout {
    private final int sensorStrength;
    private final int cloaking;
    private final int stealth;

    public ElectronicsLayout(int sensorStrength, int cloaking, int stealth) {
        this.sensorStrength = sensorStrength;
        this.cloaking = cloaking;
        this.stealth = stealth;

    }

    public int getSensorStrength() {
        return sensorStrength;
    }

    public int getCloaking() {
        return cloaking;
    }

    public int getStealth() {
        return stealth;
    }

}
