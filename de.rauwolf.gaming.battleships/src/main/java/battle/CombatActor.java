package main.java.battle;

public abstract class CombatActor {
    private final int timeCost;
    private int       currentInitiative;
    private int       lostTicks;

    protected abstract CombatTarget takeAction(SingleBattle currentBattle);

    protected CombatActor(int startingInitiative, int timeCost) {
        this.timeCost = timeCost;
        this.currentInitiative = startingInitiative;
    }

    protected final int loseTicks() {
        int before = currentInitiative;
        currentInitiative -= timeCost;
        return before;
    }

    public int getCurrentInitiative() {
        return currentInitiative;
    }

    public void setCurrentInitiative(int currentInitiative) {
        this.currentInitiative = currentInitiative;
    }

    public void rememberLostTicks(int lostTicks) {
        this.lostTicks += lostTicks;
    }

    public boolean applyRememberedLostTicks() {
        final boolean retVal = lostTicks > 0;
        currentInitiative -= lostTicks;
        lostTicks = 0;
        return retVal;
    }
}
