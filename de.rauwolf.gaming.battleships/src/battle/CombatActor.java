package battle;

import logging.battleLogger.BattleLogger;

public abstract class CombatActor implements Comparable<CombatActor> {
    private final int      initiativeDecay;

    private int            currentInitiative;

    protected SingleBattle currentBattle;
    protected BattleLogger logger;

    protected CombatActor(int startInitiative, int initiativeDecay) {
        this.setCurrentInitiative(startInitiative);
        this.initiativeDecay = initiativeDecay;
    }

    protected final void loseInitiative() {
        setCurrentInitiative(getCurrentInitiative() - initiativeDecay);
    }

    public final void setCurrentBattle(SingleBattle singleBattle) {
        this.currentBattle = singleBattle;
    }

    public final void addBattleLoger(BattleLogger battleLogger) {
        this.logger = battleLogger;
    }

    abstract CombatTarget takeAction();

    @Override
    public int compareTo(CombatActor other) {
        return other.getCurrentInitiative() - this.getCurrentInitiative();
    }

	public int getCurrentInitiative() {
		return currentInitiative;
	}

	public void setCurrentInitiative(int currentInitiative) {
		this.currentInitiative = currentInitiative;
	}
}
