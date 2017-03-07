package ships.blueprints;

public class PropulsionLayout {
    private final String name;

    private final int    evasion;
    private final int    starlaneSpeed;

    public PropulsionLayout(String name, int evasion, int starlaneSpeed) {
        this.name = name;

        this.evasion = evasion;
        this.starlaneSpeed = starlaneSpeed;
    }

    public String getName() {
        return name;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getStarlaneSpeed() {
        return starlaneSpeed;
    }

}
