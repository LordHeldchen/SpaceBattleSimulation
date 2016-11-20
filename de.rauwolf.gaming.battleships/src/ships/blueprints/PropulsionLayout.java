package ships.layouts;

public class PropulsionLayout {
    private final String name;

    private final int    battleSpeed;
    private final int    battleSpeedDecay;
    private final int    evasion;
    private final int    starlaneSpeed;

    public PropulsionLayout(String name, int battleSpeed, int battleSpeedDecay, int evasion, int starlaneSpeed) {
        this.name = name;

        this.battleSpeed = battleSpeed;
        this.battleSpeedDecay = battleSpeedDecay;
        this.evasion = evasion;
        this.starlaneSpeed = starlaneSpeed;
    }

    public String getName() {
        return name;
    }

    public int getBattleSpeed() {
        return battleSpeed;
    }

    public int getBattleSpeedDecay() {
        return battleSpeedDecay;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getStarlaneSpeed() {
        return starlaneSpeed;
    }

}
